package ch.hslu.appe.assortment.global;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.entities.Article;
import io.opentracing.Span;

/**
 * Responsible for dealing with global assortment articles.
 */
public interface GlobalAssortmentService {
    /**
     * Retrieves all articles.
     * @param span the span used to trace the request.
     * @return the articles.
     * @throws IOException when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    List<Article> getAllArticles(final Span span) throws IOException, InterruptedException;
}
