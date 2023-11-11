package ch.hslu.appe.assortment.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to reserve articles through the REST API.
 */
public final class ReserveArticlesDTO {
    private List<ArticleReservationRequest> reservations = new ArrayList<>();

    /**
     * Get the articles to reserve.
     * @return the articles to reserve.
     */
    public List<ArticleReservationRequest> getReservations() {
        return reservations;
    }

    /**
     * Set the articles to reserve.
     * @param reservations the articles to reserve.
     */
    public void setReservations(List<ArticleReservationRequest> reservations) {
        this.reservations = reservations;
    }
}
