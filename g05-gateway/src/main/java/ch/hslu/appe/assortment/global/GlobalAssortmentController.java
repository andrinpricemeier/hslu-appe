package ch.hslu.appe.assortment.global;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Article;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the endpoint for global assortment articles.
 */
@Controller("/api/v1/assortment/global/articles")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class GlobalAssortmentController {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalAssortmentController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final GlobalAssortmentService service;

    public GlobalAssortmentController(final GlobalAssortmentService service) {
        this.service = service;
    }

    /**
     * Retrieves all articles.
     * 
     * @return the articles including the appropriate http status.
     */
    @Get("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<List<Article>> getAllArticles() {
        Span span = null;
        try {
            span = tracer.buildSpan("get articles").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Retrieving global assortment articles");
            final var articles = service.getAllArticles(span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(articles);
        } catch (Exception e) {
            LOG.error("Retrieving global assortment articles failed.", e);
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
