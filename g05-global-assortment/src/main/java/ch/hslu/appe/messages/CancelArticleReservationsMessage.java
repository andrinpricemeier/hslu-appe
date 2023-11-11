package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message received to cancel reservations.
 */
public final class CancelArticleReservationsMessage extends Message {
    private List<Long> reservations = new ArrayList<>();

    public List<Long> getReservations() {
        return reservations;
    }

    public void setReservations(List<Long> reservations) {
        this.reservations = reservations;
    }    
}
