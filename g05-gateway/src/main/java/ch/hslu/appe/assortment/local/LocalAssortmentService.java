package ch.hslu.appe.assortment.local;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.entities.Article;
import io.opentracing.Span;

/**
 * Deals with local assortment articles.
 */
public interface LocalAssortmentService {
    /**
     * Gets all articles.
     * 
     * @param span the span used to trace the request.
     * @return the articles.
     * @throws IOException          when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    List<Article> getAllArticles(final Span span) throws IOException, InterruptedException;

    /**
     * Adds stock to the given article.
     * 
     * @param articleId the article to increment the stock for.
     * @param amount    the amount to increase the stock by.
     * @param span      the span used to trace the request.
     * @throws IOException          when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    void addStock(final int articleId, final int amount, final Span span) throws IOException, InterruptedException;
}
