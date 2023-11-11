package ch.hslu.appe.assortment.local.reservations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.dtos.CancelArticleReservationsDTO;
import ch.hslu.appe.assortment.dtos.ReserveArticlesDTO;
import ch.hslu.appe.assortment.messages.ReservedArticleResponse;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the REST API for local assortment reservations.
 */
@Controller("/api/v1/assortment/local/reservations")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LocalReservationController {
    private static final Logger LOG = LoggerFactory.getLogger(LocalReservationController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final LocalReservationService service;

    /**
     * Creates a new instance.
     * 
     * @param service the service to deal with reservations.
     */
    public LocalReservationController(final LocalReservationService service) {
        this.service = service;
    }

    /**
     * Reserves articles.
     * @param reserveArticles the articles to reserve in the local assortment.
     * @return the HTTP status of the outcome.
     */
    @Post("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<List<ReservedArticleResponse>> reserveArticles(ReserveArticlesDTO reserveArticles) {
        Span span = null;
        try {
            span = tracer.buildSpan("reserve articles").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Reserving local assortment articles");
            final var reservedArticles = service.reserveArticles(reserveArticles.getReservations(), span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(reservedArticles);
        } catch (Exception e) {
            LOG.error("Retrieving local assortment reservations failed.", e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            return HttpResponse.serverError();
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }

    /**
     * Cancels reservations in the local assortment.
     * @param cancellation the articles to cancel.
     * @return the HTTP status of the outcome.
     */
    @Post("/cancellations")
    @Secured({ "Verkäufer" })
    public HttpResponse<Object> cancelReservations(CancelArticleReservationsDTO cancellation) {
        Span span = null;
        try {
            span = tracer.buildSpan("cancelling reservations").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Cancelling local assortment reservations");
            service.cancelReservations(cancellation.getReservations(), span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error("Cancelling local assortment reservations failed.", e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            return HttpResponse.serverError();
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }
}
