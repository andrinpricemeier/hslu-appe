package ch.hslu.appe.repositories;

import java.util.List;

import ch.hslu.appe.entities.Article;

/**
 * Responsible for dealing with collections of articles.
 */
public interface ArticleRepository {
    /**
     * Retrieves all articles.
     * @return the articles.
     */
    List<Article> getAll();
    /**
     * Reserve an article.
     * @param articleNr the article.
     * @param amount the amount to reserve.
     * @return the reservation number.
     * @throws ArticleReservationFailedException thrown when the reservation fails. This can be due to a stock that is too low.
     */
    long reserve(int articleNr, int amount) throws ArticleReservationFailedException;
    /**
     * Cancels/frees the given reservation.
     * A reservation number that doesn't exist is ignored silently.
     * @param reservationNr
     */
    void freeReservation(long reservationNr);
    /**
     * Orders the article specified by the given reservation number.
     * @param reservationNr the reservation number.
     * @return the amount of articles ordered.
     */
    int order(long reservationNr);
    /**
     * Returns the article for the given reservation number.
     * @param reservationNr the reservation number.
     * @return the article.
     */
    Article getByReservationNr(final long reservationNr);
}
