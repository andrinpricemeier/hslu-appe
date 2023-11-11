package ch.hslu.appe.assortment.local.reservations;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.dtos.ArticleReservationRequest;
import ch.hslu.appe.assortment.messages.ReservedArticleResponse;
import io.opentracing.Span;

/**
 * Deals with reservations in the local assortment.
 */
public interface LocalReservationService {
    /**
     * Reserves articles.
     * @param reservationRequests the articles to reserve.
     * @param span the span used to trace the request.
     * @return the reserved articles including reservation numbers.
     * @throws IOException when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    List<ReservedArticleResponse> reserveArticles(final List<ArticleReservationRequest> reservationRequests, final Span span) throws IOException, InterruptedException;
    /**
     * Cancels reservations.
     * @param reservations the rseervations to cancel.
     * @param span the span used to trace the request.
     * @throws IOException when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    void cancelReservations(final List<Long> reservations, Span span) throws IOException, InterruptedException;
}
