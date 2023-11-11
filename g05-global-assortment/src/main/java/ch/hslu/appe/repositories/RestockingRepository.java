package ch.hslu.appe.repositories;

import java.util.List;

import ch.hslu.appe.entities.Restocking;

/**
 * Represents a collection of restockings.
 */
public interface RestockingRepository {
    /**
     * Adds a new restocking.
     * @param restocking the restocking.
     */
    void add(final Restocking restocking);
    /**
     * Returns all restockings.
     * @return the restockings.
     */
    List<Restocking> getAll();
}
