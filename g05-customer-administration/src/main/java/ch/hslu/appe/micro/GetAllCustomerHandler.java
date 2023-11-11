package ch.hslu.appe.micro;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageReceiver;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.db.MongoDBCustomerRepository;
import ch.hslu.appe.entities.Customer;

import ch.hslu.appe.entities.CustomerRepository;
import ch.hslu.appe.messages.GetAllCustomer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.impl.AMQImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Handles customer.getall messages
 */

public final class GetAllCustomerHandler implements AutoCloseable, MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(GetAllCustomerHandler.class);
    private final Trace trace;
    private final String exchangeName;
    private final BusConnector bus;
    private final CustomerRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Customer> customerList;

    /**
     * Constructor
     *
     * @param repository        the customer repository.
     * @param trace             the trace to track the message.
     * @param exchangeName      the name of the queue.
     * @param bus               the bus.
     */
    public GetAllCustomerHandler(CustomerRepository repository, final Trace trace, String exchangeName, BusConnector bus) {
        this.repository = repository;
        this.trace = trace;
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    /**
     * Receives the message, retrieves the customer from the database and replies the customer to the sender of the message
     *
     * @param route   Route.
     * @param replyTo ReplyTo Route.
     * @param corrId  corrId.
     * @param message Message.
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {
        // log event
        String threadName = Thread.currentThread().getName();

        // unpack received message data
        TypeReference<GetAllCustomer> typeRef = new TypeReference<>() {};
        try{
            final GetAllCustomer getAllCustomerMessage = mapper.readValue(message, typeRef);
            final var span = trace.createSpan(getAllCustomerMessage.getSpanContext(), "get all customers");
            LOG.info("Received message customer.getAll, span context: " + getAllCustomerMessage.getSpanContext());
            customerList = repository.getAllCustomer();
            LOG.info("Retrieved all customer from database, span context: " + getAllCustomerMessage.getSpanContext());
            String data = mapper.writeValueAsString(customerList);
            span.setTag("customers.count", customerList.size());
            span.finish();
            bus.reply(exchangeName, replyTo, corrId, data);
            LOG.info("Replied to message customer.getAll, span context: " + getAllCustomerMessage.getSpanContext());
        } catch (JsonProcessingException e) {
            LOG.error(e.toString());
        } catch (IOException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * closes the bus
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.bus.close();
    }
}
