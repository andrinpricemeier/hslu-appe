package ch.hslu.appe.customers;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.customers.entities.Customer;
import io.opentracing.Span;

/**
 * Responsible for dealing with customers.
 */
public interface CustomerService {
    /**
     * Returns a list of all customers available in our system.
     * @param span the span to log the request to.
     * @return the customers.
     * @throws IOException thrown when something with the communication fails.
     * @throws InterruptedException thrown when the thread is interrupted.
     */
    List<Customer> getAll(final Span span) throws IOException, InterruptedException;
}
