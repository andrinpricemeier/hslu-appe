package ch.hslu.appe.assortment.global.restockings;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Restocking;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the REST API for dealing with global assortment restockings.
 */
@Controller("/api/v1/assortment/global/restockings")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class GlobalRestockingController {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalRestockingController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final GlobalRestockingService service;

    /**
     * Creates a new instance.
     * 
     * @param service the restocking service.
     */
    public GlobalRestockingController(final GlobalRestockingService service) {
        this.service = service;
    }

    /**
     * Get all restockings.
     * 
     * @return the restockings.
     */
    @Get("/")
    @Secured({ "Filialleiter" })
    public HttpResponse<List<Restocking>> getAllRestockings() {
        Span span = null;
        try {
            span = tracer.buildSpan("get restockings").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Retrieving global assortment restockings");
            final var restockings = service.getAllRestockings(span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(restockings);
        } catch (Exception e) {
            LOG.error("Retrieving global assortment restockings failed.", e);
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
