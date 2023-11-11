package ch.hslu.appe.assortment.global.reservations;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.dtos.ArticleReservationRequest;
import ch.hslu.appe.assortment.messages.ReservedArticleResponse;
import io.opentracing.Span;

public interface GlobalReservationService {
    List<ReservedArticleResponse> reserveArticles(final List<ArticleReservationRequest> reservationRequests, final Span span) throws IOException, InterruptedException;
    void cancelReservations(final List<Long> reservations, Span span) throws IOException, InterruptedException;
}
