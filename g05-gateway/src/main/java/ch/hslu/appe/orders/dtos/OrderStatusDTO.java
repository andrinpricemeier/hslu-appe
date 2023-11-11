package ch.hslu.appe.orders.dtos;

import ch.hslu.appe.orders.entities.OrderStatus;

/**
 * DTO used to change the order status via REST API.
 * Micronaut doesn't seem to work when sending an enum only.
 */
public final class OrderStatusDTO {
    private OrderStatus status;
    /**
     * The status.
     * @return the status.
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Set the status.
     * @param status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }    
}
