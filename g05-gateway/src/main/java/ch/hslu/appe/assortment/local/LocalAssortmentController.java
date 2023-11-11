package ch.hslu.appe.assortment.local;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.dtos.AddStockDTO;
import ch.hslu.appe.assortment.entities.Article;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the REST API for dealing with local assortment articles.
 */
@Controller("/api/v1/assortment/local/articles")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LocalAssortmentController {
    private static final Logger LOG = LoggerFactory.getLogger(LocalAssortmentController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final LocalAssortmentService service;

    public LocalAssortmentController(final LocalAssortmentService service) {
        this.service = service;
    }

    @Get("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<List<Article>> getAllArticles() {
        Span span = null;
        try {
            span = tracer.buildSpan("get articles").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Retrieving local assortment articles");
            final var articles = service.getAllArticles(span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(articles);
        } catch (Exception e) {
            LOG.error("Retrieving local assortment articles failed.", e);
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

    @Post("/{id}/stock")
    @Secured({ "Verkäufer", "Datentypist" })
    public HttpResponse<Object> addStock(final int id, final AddStockDTO addStock) {
        Span span = null;
        try {
            span = tracer.buildSpan("increase stock for articles").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Increasing stock for local assortment article {} by {}", id, addStock.getAmount());
            service.addStock(id, addStock.getAmount(), span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error("Adding stock to local assortment article failed.", e);
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
