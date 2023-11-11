package ch.hslu.appe.entities;

import com.mongodb.client.MongoDatabase;

import java.util.List;

/**
 * Responsible for saving and providing customer
 */
public interface CustomerRepository {
    void add(final Customer customer);
    List<Customer> getAllCustomer();
    MongoDatabase getMongoDatabase();
}
