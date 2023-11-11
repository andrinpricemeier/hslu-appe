package ch.hslu.appe.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hslu.appe.entities.Article;
import ch.hslu.appe.stock.api.Stock;

/**
 * Implements an article repository that encapsulates the legacy stock API and
 * creates a few articles randomly.
 */
public final class RandomInMemoryArticleRepository implements ArticleRepository {
    private final Stock stock;
    private final List<Article> articles = new ArrayList<Article>();
    private final Map<Long, Integer> reservationToArticle = new HashMap<>();
    private final static int NUMBER_OF_ARTICLES = 4;

    /**
     * Creates a new instance.
     * 
     * @param stock the legacy API.
     */
    public RandomInMemoryArticleRepository(final Stock stock) {
        this.stock = stock;
        this.init();
    }

    @Override
    public List<Article> getAll() {
        return Collections.unmodifiableList(articles);
    }

    @Override
    public long reserve(int articleNr, int amount) throws ArticleReservationFailedException {
        // Guarantee unique reservation numbers. The minimum is 10 or 25ms based on the
        // OS, to be on the safe side we wait 50ms.
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            // We don't care in this case.
        }
        final var reservationNr = stock.reserveItem(articleNr, amount);
        if (reservationNr == -1) {
            throw new ArticleReservationFailedException();
        }
        refreshStock();
        reservationToArticle.put(reservationNr, articleNr);
        return reservationNr;
    }

    @Override
    public void freeReservation(long reservationNr) {
        if (reservationToArticle.containsKey(reservationNr)) {
            reservationToArticle.remove(reservationNr);
        }
        // If the ticket is invalid we don't care anyway because it could be that we
        // received the same cancellation request multiple times.
        stock.freeReservation(reservationNr);
        refreshStock();
    }

    @Override
    public int order(long reservationNr) {
        if (reservationToArticle.containsKey(reservationNr)) {
            reservationToArticle.remove(reservationNr);
        }
        return stock.orderReservation(reservationNr);
    }

    @Override
    public Article getByReservationNr(final long reservationNr) {
        final Integer articleNr = reservationToArticle.get(reservationNr);
        if (articleNr == null) {
            return null;
        }
        return getByArticleNr(articleNr);
    }

    private Article getByArticleNr(final int articleNr) {
        return articles.stream().filter(article -> article.getArticleNr() == articleNr).findFirst().get();
    }

    private void refreshStock() {
        articles.forEach(article -> article.setStock(stock.getItemCount(article.getArticleNr())));
    }

    private void init() {
        final var articleNrGenerator = new ArticleNrGenerator();
        for (int i = 0; i < NUMBER_OF_ARTICLES; i++) {
            articleNrGenerator.generate();
            articles.add(createRandomArticle(articleNrGenerator.getLastGeneratedArticleNr()));
        }
    }

    private Article createRandomArticle(int articleNr) {
        final var article = new Article();
        article.setArticleNr(articleNr);
        article.setDescription(String.format("Food Item Nr. %s", articleNr));
        // Calling stock.getItemCount() causes the article to be added to the
        // stock/assortment.
        article.setStock(stock.getItemCount(articleNr));
        article.setPrice(20.0);
        return article;
    }
}
