package ch.hslu.appe.orders.messages;

import ch.hslu.appe.bus.Message;

/** 
 * Message to be sent when the customer has executed an order.
 * This message is meant to be chance for other systems to react to this request.
 */
public final class OrderExecutedMessage extends Message {
    private String orderId;

    /**
     * Get the order id.
     * @return the order id.
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Set the order id.
     * @param orderId the order id.
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    } 
        
}
