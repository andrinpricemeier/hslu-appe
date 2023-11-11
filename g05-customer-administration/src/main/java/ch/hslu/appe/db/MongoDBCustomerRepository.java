/*
Quelle: https://developer.mongodb.com/quickstart/java-setup-crud-operations/?utm_campaign=javainsertingdocuments&utm_source=facebook&utm_medium=organic_social

 */

package ch.hslu.appe.db;

import ch.hslu.appe.entities.Customer;

import ch.hslu.appe.entities.CustomerRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific implementation with a mongo database of CustomerRepository
 */

public class MongoDBCustomerRepository implements CustomerRepository {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> customerCollection;
    private final String connectionString;

    /**
     * Creates a new mongodb collection
     *
     * @param user          user for the authentication to the database, can be empty
     * @param password      password for the authentication to the database, can be empty
     * @param url           url of the mongodb instance
     * @param port          port of the mongodb instance
     */
    public MongoDBCustomerRepository(String user, String password, String url, int port) {
        if ("".equals(user) || "".equals(password)) {
            connectionString = "mongodb://" + url + ":" + port;
        } else {
            connectionString = "mongodb://" + user + ":" + password + "@" + url + ":" + port;
        }
        mongoClient = MongoClients.create(connectionString);
        mongoDatabase = mongoClient.getDatabase("customer-mongodb");
        customerCollection = mongoDatabase.getCollection("customers");
    }

    /**
     * reates a new mongodb collection
     *
     * @param user          user for the authentication to the database, can be empty
     * @param password      password for the authentication to the database, can be empty
     * @param url           url of the mongodb instance
     * @param port          port of the mongodb instance
     * @param dbName        name of the collection in the mongodb instance
     */
    public MongoDBCustomerRepository(String user, String password, String url, int port, String dbName) {
        if ("".equals(user) || "".equals(password)) {
            connectionString = "mongodb://" + url + ":" + port;
        } else {
            connectionString = "mongodb://" + user + ":" + password + "@" + url + ":" + port;
        }
        mongoClient = MongoClients.create(connectionString);
        mongoDatabase = mongoClient.getDatabase(dbName);
        customerCollection = mongoDatabase.getCollection("customers");
    }

    /**
     * add a Customer to the mongodb collection
     * @param customer      customer object
     */
    public void add(Customer customer) {
        Document customerDocument = new Document("_id", new ObjectId());
        customerDocument.append("customer_customerId", customer.getCustomerId())
                .append("customer_firstname", customer.getFirstName())
                .append("customer_lastname", customer.getLastName())
                .append("customer_street", customer.getStreet())
                .append("customer_number", customer.getNumber())
                .append("customer_postalcode", customer.getPostalCode())
                .append("customer_city", customer.getCity());
        customerCollection.insertOne(customerDocument);
    }

    /**
     * returns all customer from the mongodb collection
     *
     * @return      list of customer
     */
    public List<Customer> getAllCustomer() {
        List<Customer> customerList = new ArrayList<>();
        List<Document> customerDocumentList = customerCollection.find().into(new ArrayList<>());

        for (Document customerDoc : customerDocumentList) {
            Customer customer = new Customer(customerDoc.getInteger("customer_customerId"),
                    customerDoc.getString("customer_firstname"),
                    customerDoc.getString("customer_lastname"),
                    customerDoc.getString("customer_street"),
                    customerDoc.getString("customer_number"),
                    customerDoc.getString("customer_postalcode"),
                    customerDoc.getString("customer_city"));
            customerList.add(customer);
        }
        return customerList;
    }

    /**
     * returns the database from the mongodb instance
     *
     * @return      MongoDatabase object
     */
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
