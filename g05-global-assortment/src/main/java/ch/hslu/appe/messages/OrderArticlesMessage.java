package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to order articles.
 */
public final class OrderArticlesMessage extends Message {
    private String orderId;
    private List<Long> articleReservations = new ArrayList<>();    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Long> getArticleReservations() {
        return articleReservations;
    }

    public void setArticleReservations(List<Long> articleReservations) {
        this.articleReservations = articleReservations;
    }
}
