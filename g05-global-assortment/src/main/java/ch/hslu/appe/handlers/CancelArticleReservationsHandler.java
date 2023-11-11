package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.CancelArticleReservationsMessage;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleRepository;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Responsible for cancelling articles.
 */
public final class CancelArticleReservationsHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CancelArticleReservationsHandler.class);
    private final ArticleRepository repository;
    private final Trace trace;
    private final Monitoring monitoring;

    /**
     * Creates a new instance.
     * 
     * @param repository the article repository.
     * @param trace      the trace used to track messages.
     */
    public CancelArticleReservationsHandler(final ArticleRepository repository, final Trace trace, final Monitoring monitoring) {
        this.repository = repository;
        this.trace = trace;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            span = trace.createFollowsFrom(message.getSpanContext(), "cancelling article reservations");
            final var cmd = (CancelArticleReservationsMessage) message;
            monitoring.logEvent("N/A", "Bestellung", "Reservierungen", String.format("Es wurden %s Artikelreservationen im Zentrallager freigegeben.", cmd.getReservations().size()));
            LOG.info("Cancelling {} reservations.", cmd.getReservations().size());
            cmd.getReservations().forEach(reservationNr -> repository.freeReservation(reservationNr));
            span.setTag("articles.reservations.cancelled", cmd.getReservations().size());
        } catch (Exception e) {
            LOG.error("Cancelling reservations failed.", e);
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
