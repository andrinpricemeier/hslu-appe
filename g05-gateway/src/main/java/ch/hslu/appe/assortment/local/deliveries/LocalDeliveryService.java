package ch.hslu.appe.assortment.local.deliveries;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.entities.Delivery;
import io.opentracing.Span;

/**
 * Deals with deliveries.
 */
public interface LocalDeliveryService {
    /**
     * Retrieves all deliveries.
     * @param span span used to trace requests.
     * @return the deliveries.
     * @throws IOException when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    List<Delivery> getAllDeliveries(final Span span) throws IOException, InterruptedException;
}
