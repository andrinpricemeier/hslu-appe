package ch.hslu.appe.assortment.messages;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.assortment.dtos.ArticleReservationRequest;
import ch.hslu.appe.bus.Message;

/**
 * Sent to reserve articles so that they can be ordered later on.
 */
public final class ReserveArticlesMessage extends Message {
    private List<ArticleReservationRequest> reservations = new ArrayList<>();

    /**
     * Get the articles to reserve.
     * @return the reservations.
     */
    public List<ArticleReservationRequest> getReservations() {
        return reservations;
    }

    /**
     * Set the articles to reserve.
     * @param reservations the reservations.
     */
    public void setReservations(List<ArticleReservationRequest> reservations) {
        this.reservations = reservations;
    }
}
