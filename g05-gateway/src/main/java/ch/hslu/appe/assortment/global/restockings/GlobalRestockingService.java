package ch.hslu.appe.assortment.global.restockings;

import java.io.IOException;
import java.util.List;

import ch.hslu.appe.assortment.entities.Restocking;
import io.opentracing.Span;

/**
 * Deals with restockings in the global assortment.
 */
public interface GlobalRestockingService {
    /**
     * Gets all restockings.
     * @param span the span used to trace the request.
     * @return the restockings.
     * @throws IOException when communication fails.
     * @throws InterruptedException when the thread is interrupted.
     */
    List<Restocking> getAllRestockings(final Span span) throws IOException, InterruptedException;
}
