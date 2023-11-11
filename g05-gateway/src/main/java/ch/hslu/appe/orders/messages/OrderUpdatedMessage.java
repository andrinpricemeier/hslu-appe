package ch.hslu.appe.orders.messages;

import ch.hslu.appe.bus.Message;
import ch.hslu.appe.orders.entities.Order;

/**
 * Message to be sent when the customer has changed an order.
 * This message gives other services the chance to update their state.
 */
public final class OrderUpdatedMessage extends Message {
    private Order order = new Order();

    /**
     * Get the order.
     * @return the order.
     */
    public Order getOrder() {
        return order;
    }
    /**
     * Set the order.
     * @param order the order.
     */
    public void setOrder(Order order) {
        this.order = order;
    }
}
