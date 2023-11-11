package ch.hslu.appe.orders;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.orders.entities.Order;
import ch.hslu.appe.orders.messages.ListOrdersMessage;
import ch.hslu.appe.orders.messages.OrderCancelledMessage;
import ch.hslu.appe.orders.messages.OrderCreatedMessage;
import ch.hslu.appe.orders.messages.OrderExecutedMessage;
import ch.hslu.appe.orders.messages.OrderUpdatedMessage;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Represents a service for dealing with orders, communicating over a RabbitMQ
 * bus.
 */
@Singleton
public final class BusOrderService implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(BusOrderService.class);
    private final BusConnector bus;
    // Configure the mapper in such a way that we're flexible with differences
    // between message across service boundaries.
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusOrderService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Order> getAll(final Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all orders.");
        final var msg = new ListOrdersMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        final String response = bus.talkSync(exchangeName, MessageRoutes.ORDER_LIST, mapper.writeValueAsString(msg));
        return mapper.readValue(response, new TypeReference<List<Order>>() {
        });
    }

    @Override
    public void createOrder(Order order, final Span span) throws IOException {
        LOG.info("Creating an order with id {}", order.getOrderId());
        final var msg = new OrderCreatedMessage();
        msg.setOrder(order);
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        bus.talkAsync(exchangeName, MessageRoutes.ORDER_CREATED, mapper.writeValueAsString(msg), order.getOrderId());
    }

    @Override
    public void executeOrder(final String id, final Span span) throws IOException {
        LOG.info("Executing order {}", id);
        final var msg = new OrderExecutedMessage();
        msg.setOrderId(id);
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        bus.talkAsync(exchangeName, MessageRoutes.ORDER_EXECUTED, mapper.writeValueAsString(msg), id);
    }

    @Override
    public void cancelOrder(final String id, final Span span) throws IOException {
        LOG.info("Cancellling order {}", id);
        final var msg = new OrderCancelledMessage();
        msg.setOrderId(id);
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        bus.talkAsync(exchangeName, MessageRoutes.ORDER_CANCELLED, mapper.writeValueAsString(msg), id);
    }

    @Override
    public void updateOrder(final Order order, final Span span) throws IOException {
        LOG.info("Updating order {}", order.getOrderId());
        final var msg = new OrderUpdatedMessage();
        msg.setOrder(order);
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        bus.talkAsync(exchangeName, MessageRoutes.ORDER_UPDATED, mapper.writeValueAsString(msg), order.getOrderId());
    }
}
