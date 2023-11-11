package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.ListArticlesMessage;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleRepository;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handles requests for retrieving articles.
 */
public final class ListArticlesHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ListArticlesHandler.class);
    private final ArticleRepository repository;
    private final MessageBus bus;
    private final Trace trace;
    private final Monitoring monitoring;

    /**
     * Creates a new instance.
     * 
     * @param repository the article repository.
     * @param bus        the bus.
     * @param trace      the trace to track messages.
     */
    public ListArticlesHandler(final ArticleRepository repository, final MessageBus bus, final Trace trace, final Monitoring monitoring) {
        this.repository = repository;
        this.bus = bus;
        this.trace = trace;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            monitoring.logEvent("N/A", "Bestellung", "Bestellung bearbeiten", "Der Zentrallagerbestand wurde abgefragt.");
            span = trace.createChildOf(message.getSpanContext(), "list articles");
            final var cmd = (ListArticlesMessage) message;
            LOG.info("Retrieving articles");
            final var articles = repository.getAll();
            span.setTag("articles.count", articles.size());
            bus.reply(cmd.getReplyTo(), cmd.getCorrelationId(), articles);
        } catch (Exception e) {
            LOG.error("Retrieving articles failed.", e);
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
