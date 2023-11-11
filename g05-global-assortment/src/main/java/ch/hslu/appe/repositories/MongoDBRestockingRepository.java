package ch.hslu.appe.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.entities.Restocking;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the restocking repository by using a MongoDB.
 */
public final class MongoDBRestockingRepository implements RestockingRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MongoDBRestockingRepository.class);
    private final MongoClient mongoClient;
    private final MongoDatabase db;
    private final MongoCollection<Restocking> restockings;

    /**
     * Creates a new class.
     * 
     * @param url      the url of the MongoDB instance.
     * @param port     the port of the MongoDB instance.
     * @param user     the user to authenticate ourselves with. Can be empty.
     * @param password the password to authenticate ourselves with. Can be empty.
     */
    public MongoDBRestockingRepository(String url, int port, String user, String password) {
        String authString = null;
        if (!"".equals(user)) {
            authString = user;
        }
        if (!"".equals(password)) {
            authString += ":" + password;
        }
        if (authString != null) {
            authString += "@";
        } else {
            authString = "";
        }
        // The codec registry allows us to directly save POJOs without mapping them to
        // generic documents first.
        final CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        final var connectionString = new ConnectionString(String.format("mongodb://%s%s:%s", authString, url, port));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry).build();
        LOG.info("Using connection string to mongodb: {}", connectionString.toString());
        mongoClient = MongoClients.create(settings);
        db = mongoClient.getDatabase("globalassortment-db");
        restockings = db.getCollection("restockings", Restocking.class);
    }

    @Override
    public void add(Restocking restocking) {
        restockings.insertOne(restocking);
    }

    @Override
    public List<Restocking> getAll() {
        return restockings.find().into(new ArrayList<Restocking>());
    }
}
