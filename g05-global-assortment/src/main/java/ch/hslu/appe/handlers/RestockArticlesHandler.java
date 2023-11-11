package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.messages.RestockArticlesMessage;
import ch.hslu.appe.messages.RestockFulfilledMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.RestockingRepository;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Deals with restocking articles.
 */
public final class RestockArticlesHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestockArticlesHandler.class);
    private final RestockingRepository repository;
    private final Trace trace;
    private final MessageBus bus;
    private final Monitoring monitoring;

    public RestockArticlesHandler(final RestockingRepository repository, final Trace trace, final MessageBus bus, final Monitoring monitoring) {
        this.repository = repository;
        this.trace = trace;
        this.bus = bus;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            final var cmd = (RestockArticlesMessage) message;
            monitoring.logEvent("N/A", "Lagerverwaltung", "Nachbestellungen", String.format("Im Zentrallager wurde eine Nachbestellung von %s Artikeln aufgegeben.", cmd.getArticles().size()));
            span = trace.createFollowsFrom(message.getSpanContext(), "restock articles");
            LOG.info("Restocking articles");
            cmd.getArticles().forEach(article -> repository.add(article));
            span.setTag("articles.restocked", cmd.getArticles().size());
            final var fulfillment = new RestockFulfilledMessage();
            fulfillment.setSpanContext(trace.extractContext(span));
            fulfillment.setRestockings(cmd.getArticles());
            // So that we have something to show in the GUI, we immediately fulfill the
            // restocking and inform all interested parties.
            bus.publish(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESTOCK_FULFILLED, fulfillment);
        } catch (Exception e) {
            LOG.error("Restocking articles failed.", e);
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
