package ch.hslu.appe.assortment.dtos;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.bus.Message;

/**
 * Represents a request to free up reservations through the REST API.
 */
public final class CancelArticleReservationsDTO extends Message {
    private List<Long> reservations = new ArrayList<>();

    /**
     * Get the reservations to cancel.
     * @return the reservations.
     */
    public List<Long> getReservations() {
        return reservations;
    }

    /**
     * Set the reservations to cancel.
     * @param reservations the reservations.
     */
    public void setReservations(List<Long> reservations) {
        this.reservations = reservations;
    }    
}
