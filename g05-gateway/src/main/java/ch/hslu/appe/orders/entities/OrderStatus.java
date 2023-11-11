package ch.hslu.appe.orders.entities;

/**
 * Represents the status of the order.
 */
public enum OrderStatus {
    /**
     * The order has been created, but not executed.
     */
    CREATED,
    /**
     * The order is being executed. No changes are possible anymore after this.
     */
    EXECUTING,
    /**
     * Local assortment articles have been ordered, but the local assortment hasn't sent a confirmation yet.
     */
    LOCALASSORTMENT_ORDER_PENDING,
    /**
     * Local assortment articles have been successfully ordered and acknowledged.
     */
    LOCALASSORTMENT_ORDERED,    
    /**
     * Global assortment articles have been ordered, but the global assortment hasn't sent a confirmation yet.
     */
    GLOBALASSORTMENT_ORDER_PENDING,    
    /**
     * Global assortment articles have been successfully ordered and acknowledged.
     */
    GLOBALASSORTMENT_ORDERED,
    /**
     * All articles have been ordered and the order is ready to be shipped (not part of this system).
     */
    APPROVED,
    /**
     * The order has been cancelled.
     */
    CANCELLED,
    /**
     * The order has failed. This means that a step in the order process couldn't be completed successfully.
     */
    FAILED
}
