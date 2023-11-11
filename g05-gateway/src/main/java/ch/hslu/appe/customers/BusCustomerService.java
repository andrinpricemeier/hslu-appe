package ch.hslu.appe.customers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.customers.entities.Customer;
import ch.hslu.appe.customers.messages.ListCustomersMessage;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Implementation of the customer service that talks over a RabbitMQ bus.
 */
@Singleton
public final class BusCustomerService implements CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(BusCustomerService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusCustomerService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Customer> getAll(final Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all customers.");
        final var msg = new ListCustomersMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        final String response = bus.talkSync(exchangeName, MessageRoutes.CUSTOMER_GET_ALL, mapper.writeValueAsString(msg));
        return mapper.readValue(response, new TypeReference<List<Customer>>() {});
    }
}
