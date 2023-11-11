package ch.hslu.appe.handlers;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageHandler;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.entities.ReservedArticle;
import ch.hslu.appe.messages.Message;
import ch.hslu.appe.messages.ReserveArticlesMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleRepository;
import ch.hslu.appe.repositories.ArticleReservationFailedException;
import io.opentracing.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ReserveArticlesHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ReserveArticlesHandler.class);
    private final ArticleRepository repository;
    private final MessageBus bus;
    private final Trace trace;
    private final Monitoring monitoring;

    public ReserveArticlesHandler(final ArticleRepository repository, final MessageBus bus, final Trace trace, final Monitoring monitoring) {
        this.repository = repository;
        this.bus = bus;
        this.trace = trace;
        this.monitoring = monitoring;
    }

    @Override
    public void handle(Message message) throws IOException {
        Span span = null;
        try {
            span = trace.createChildOf(message.getSpanContext(), "reserve articles");
            final var cmd = (ReserveArticlesMessage) message;
            monitoring.logEvent("N/A", "Bestellung", "Reservierungen", String.format("Im Zentrallager wurden %s Artikel reserviert.", cmd.getReservations().size()));
            LOG.info("Reserving {} articles", cmd.getReservations().size());
            final List<ReservedArticle> reservedArticles = new ArrayList<>();
            for (final var reservationRequest : cmd.getReservations()) {
                try {
                    long reservationNr = repository.reserve(reservationRequest.getArticleNr(),
                            reservationRequest.getAmount());
                    final var reservedArticle = new ReservedArticle();
                    reservedArticle.setArticleNr(reservationRequest.getArticleNr());
                    reservedArticle.setReservationNr(reservationNr);
                    reservedArticles.add(reservedArticle);
                } catch (ArticleReservationFailedException e) {
                    final var reservedArticle = new ReservedArticle();
                    reservedArticle.setArticleNr(reservationRequest.getArticleNr());
                    reservedArticle.setReservationNr(ReservedArticle.INVALID_RESERVATION);
                    reservedArticles.add(reservedArticle);
                }
            }
            span.setTag("articles.reservation.succeeded", reservedArticles.stream()
                    .filter(r -> r.getReservationNr() != ReservedArticle.INVALID_RESERVATION).count());
            span.setTag("articles.reservation.failed", reservedArticles.stream()
                    .filter(r -> r.getReservationNr() == ReservedArticle.INVALID_RESERVATION).count());
            bus.reply(cmd.getReplyTo(), cmd.getCorrelationId(), reservedArticles);
        } catch (Exception e) {
            LOG.error("Reserving articles failed.", e);
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
