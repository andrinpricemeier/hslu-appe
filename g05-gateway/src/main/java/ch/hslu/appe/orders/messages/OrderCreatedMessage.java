package ch.hslu.appe.orders.messages;

import ch.hslu.appe.bus.Message;
import ch.hslu.appe.orders.entities.Order;

/**
 * Message to be sent when a customer created an order.
 */
public final class OrderCreatedMessage extends Message {
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
