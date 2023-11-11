package ch.hslu.appe.orders;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.orders.entities.Order;
import io.opentracing.Span;

/**
 * Responsible for dealing with orders.
 */
public interface OrderService {
    /**
     * Retrieves all orders.
     * @param span span used to trace the request.
     * @return the orders.
     * @throws IOException when communication fails
     * @throws InterruptedException when the thread is interrupted.
     */
    List<Order> getAll(final Span span) throws IOException, InterruptedException;
    /**
     * Creates a new order.
     * @param order the order to create.
     * @param span the span used to trace the request.
     * @throws IOException when communication fails.
     */
    void createOrder(final Order order, final Span span) throws IOException;
    /**
     * Executes the order.
     * @param id the order id to execute.
     * @param span the span used to trace the request.
     * @throws IOException when communication fails.
     */
    void executeOrder(final String id, final Span span) throws IOException;
    /**
     * Cancels the order.
     * @param id the order id to cancel.
     * @param span the span used to trace the request.
     * @throws IOException when communication fails.
     */
    void cancelOrder(final String id, final Span span) throws IOException;
    /**
     * Updates the order.
     * @param order the updated order.
     * @param span the span used to trace the request.
     * @throws IOException when communication fails.
     */
    void updateOrder(final Order order, final Span span) throws IOException;
}
