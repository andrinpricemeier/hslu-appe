package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.ListRestockingsMessage;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.RestockingRepository;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Responsible for retrieving / handling restocking list requests.
 */
public final class ListRestockingsHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ListRestockingsHandler.class);
    private final RestockingRepository repository;
    private final MessageBus bus;
    private final Trace trace;
    private final Monitoring monitoring;

    /**
     * Creates a new instance.
     * 
     * @param repository the restocking repository.
     * @param bus        the bus.
     * @param trace      the trace used to track messages.
     */
    public ListRestockingsHandler(final RestockingRepository repository, final MessageBus bus, final Trace trace, final Monitoring monitoring) {
        this.repository = repository;
        this.bus = bus;
        this.trace = trace;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            span = trace.createChildOf(message.getSpanContext(), "list restockings");
            final var cmd = (ListRestockingsMessage) message;
            monitoring.logEvent("N/A", "Lagerverwaltung", "Nachbestellungen", "Die Nachbestellungen wurden im Zentrallager abgefragt.");
            LOG.info("Retrieving restockings");
            final var restockings = repository.getAll();
            span.setTag("restockings.count", restockings.size());
            bus.reply(cmd.getReplyTo(), cmd.getCorrelationId(), restockings);
        } catch (Exception e) {
            LOG.error("Retrieving restockings failed.", e);
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
