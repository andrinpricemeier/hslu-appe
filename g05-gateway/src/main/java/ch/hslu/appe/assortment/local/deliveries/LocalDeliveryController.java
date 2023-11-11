package ch.hslu.appe.assortment.local.deliveries;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Delivery;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the REST API for deliveries to the local assortment.
 */
@Controller("/api/v1/assortment/local/deliveries")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LocalDeliveryController {
    private static final Logger LOG = LoggerFactory.getLogger(LocalDeliveryController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final LocalDeliveryService service;

    /**
     * Creates a new instance.
     * 
     * @param service the service used to deal with deliveries.
     */
    public LocalDeliveryController(final LocalDeliveryService service) {
        this.service = service;
    }

    /**
     * Get the deliveries.
     * @return the deliveries.
     */
    @Get("/")
    @Secured({ "Filialleiter", "Datentypist" })
    public HttpResponse<List<Delivery>> getAllDeliveries() {
        Span span = null;
        try {
            span = tracer.buildSpan("get deliveries").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Retrieving local assortment deliveries");
            final var deliveries = service.getAllDeliveries(span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(deliveries);
        } catch (Exception e) {
            LOG.error("Retrieving local assortment deliveries failed.", e);
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
