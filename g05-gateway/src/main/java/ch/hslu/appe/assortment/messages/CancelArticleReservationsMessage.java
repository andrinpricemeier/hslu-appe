package ch.hslu.appe.assortment.messages;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.bus.Message;

/**
 * Sent for cancelling reservations.
 */
public final class CancelArticleReservationsMessage extends Message {
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
