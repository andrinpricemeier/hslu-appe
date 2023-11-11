package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message to reserve articles.
 */
public final class ReserveArticlesMessage extends Message {
    private List<ArticleReservationRequest> reservations = new ArrayList<>();

    public List<ArticleReservationRequest> getReservations() {
        return reservations;
    }

    public void setReservations(List<ArticleReservationRequest> reservations) {
        this.reservations = reservations;
    }
}
