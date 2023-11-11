package ch.hslu.appe.assortment.global.reservations;

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
 * Represents the REST API for global assortment reservations.
 */
@Controller("/api/v1/assortment/global/reservations")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class GlobalReservationController {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalReservationController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final GlobalReservationService service;

    /**
     * Creates a new instance.
     * 
     * @param service the service for dealing with the global
     *                                assortment back-end service.
     */
    public GlobalReservationController(final GlobalReservationService service) {
        this.service = service;
    }

    /**
     * Reserves the given articles.
     * 
     * @param reserveArticles the articles to reserve.
     * @return the reserved articles.
     */
    @Post("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<List<ReservedArticleResponse>> reserveArticles(ReserveArticlesDTO reserveArticles) {
        Span span = null;
        try {
            span = tracer.buildSpan("reserve articles").start();
            LOG.info("Reserving global assortment articles");
            final var reservedArticles = service.reserveArticles(reserveArticles.getReservations(),
                    span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(reservedArticles);
        } catch (Exception e) {
            LOG.error("Reserving global assortment articles failed.", e);
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
     * Cancels reservations.
     * 
     * @param cancellation the article reservations to cancel.
     * @return the http status of the result.
     */
    @Post("/cancellations")
    @Secured({ "Verkäufer" })
    public HttpResponse<Object> cancelReservations(CancelArticleReservationsDTO cancellation) {
        Span span = null;
        try {
            span = tracer.buildSpan("cancelling reservations").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Cancelling global assortment reservations");
            service.cancelReservations(cancellation.getReservations(), span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error("Cancelling global assortment reservations failed.", e);
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
