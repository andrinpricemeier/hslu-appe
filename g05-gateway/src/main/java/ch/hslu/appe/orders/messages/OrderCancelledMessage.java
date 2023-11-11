package ch.hslu.appe.orders.messages;

import ch.hslu.appe.bus.Message;

/**
 * Message sent to notify our system that an order has been cancelled by the customer.
 * The naming is meant to signify its asynchronous nature.
 * Another possibility would have been to name it in the form of a command, e.g. "CancelOrderMessage".
 * Because the responsibility of doing the right thing lies with the receiver of this message, it has been
 * named as an event. 
 */
public final class OrderCancelledMessage extends Message {
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    } 
        
}
