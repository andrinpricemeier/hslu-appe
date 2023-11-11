package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.entities.ArticleOrder;
import ch.hslu.appe.messages.ArticlesOrderedMessage;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.messages.OrderArticlesMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleRepository;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Responsible for ordering articles.
 */
public final class OrderArticlesHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestockArticlesHandler.class);
    private final ArticleRepository repository;
    private final Trace trace;
    private final MessageBus bus;
    private final Monitoring monitoring;

    public OrderArticlesHandler(final ArticleRepository repository, final Trace trace, final MessageBus bus, final Monitoring monitoring) {
        this.repository = repository;
        this.trace = trace;
        this.bus = bus;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            final var cmd = (OrderArticlesMessage) message;
            monitoring.logEvent(cmd.getOrderId(), "Bestellung", "Bestellung ausführen", String.format("Im Zentrallager wurde eine Bestellung mit %s Artikeln ausgeführt.", cmd.getArticleReservations().size()));
            span = trace.createFollowsFrom(message.getSpanContext(), "ordering articles");
            LOG.info("Ordering articles");
            final var orderedEvent = new ArticlesOrderedMessage();
            orderedEvent.setOrderId(cmd.getOrderId());
            for (final var reservation : cmd.getArticleReservations()) {
                var article = repository.getByReservationNr(reservation);
                final int count = repository.order(reservation);
                final var articleOrder = new ArticleOrder();
                articleOrder.setAmount(count);
                articleOrder.setArticle(article);
                orderedEvent.getOrderedArticles().add(articleOrder);
            }
            span.setTag("articles.ordered", cmd.getArticleReservations().size());
            bus.publish(MessageRoutes.GLOBALASSORTMENT_ARTICLE_ORDERED, orderedEvent, cmd.getOrderId());
        } catch (Exception e) {
            LOG.error("Ordering articles failed.", e);
            if (span != null) {
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            // Signal that this message failed and can be marked as NACK.
            throw e;
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }
}
