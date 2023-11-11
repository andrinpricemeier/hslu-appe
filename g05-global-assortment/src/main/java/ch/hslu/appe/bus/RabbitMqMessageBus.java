package ch.hslu.appe.bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.messages.Message;

/**
 * Implementation of the MessageBus interface that talks to RabbitMQ.
 */
public final class RabbitMqMessageBus implements MessageBus, MessageReceiver {
    private final Logger LOG = LoggerFactory.getLogger(RabbitMqMessageBus.class);
    private final Map<String, List<TypedMessageHandler>> handlers = new HashMap<>();
    private final BusConnector busConnector;
    private final String exchangeName;
    private final String queueName;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);

    public RabbitMqMessageBus(final String exchangeName, final String queueName, final BusConnector busConnector)
            throws IOException {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.busConnector = busConnector;
        busConnector.declareQueue(queueName);
    }

    @Override
    public void startListen() throws IOException {
        busConnector.listenFor(exchangeName, queueName, this);
    }

    @Override
    public <T extends Message> void registerHandler(final String route, final MessageHandler handler,
            final Class<T> messageClass) throws IOException {
        LOG.info("Registering handler for route '{}', class '{}'", route, messageClass.getSimpleName());
        var routeHandlers = handlers.get(route);
        if (routeHandlers == null) {
            routeHandlers = new ArrayList<>();
            handlers.put(route, routeHandlers);
        }
        busConnector.bind(exchangeName, queueName, route);
        // To handle serialization of messages in a generic way we require the class
        // information of the message.
        routeHandlers.add(new TypedMessageHandler(json -> {
            try {
                return (Message) mapper.readValue(json, messageClass);
            } catch (JsonProcessingException e) {
                return null;
            }
        }, handler));
    }

    @Override
    public <T extends Message> void publish(final String route, final T message) throws IOException {
        publish(route, message, null);
    }

    @Override
    public <T extends Message> void publish(final String route, final T message, final String messageId)
            throws IOException {
        LOG.info("Publishing message to {}", route);
        busConnector.talkAsync(exchangeName, route, mapper.writeValueAsString(message), messageId);
    }

    @Override
    public void reply(final String route, final String corrId, final Object anyReplyObject) throws IOException {
        LOG.info("Replying to {} with {}", route, anyReplyObject.getClass().getSimpleName());
        busConnector.reply(exchangeName, route, corrId, mapper.writeValueAsString(anyReplyObject));
        LOG.info("Message sent.");
    }

    @Override
    public void onMessageReceived(final Channel channel, final long deliveryTag, final String route,
            final String replyTo, final String corrId, final String message) {
        LOG.info("Received message to route {}", route);
        final var routeHandlers = handlers.get(route);
        if (routeHandlers == null) {
            return;
        }
        LOG.info("Found {} handlers.", routeHandlers.size());
        for (final var handler : routeHandlers) {
            try {
                final Message actualMessage = handler.getDeserializer().apply(message);
                actualMessage.setCorrelationId(corrId);
                actualMessage.setReplyTo(replyTo);
                handler.getHandler().handle(actualMessage);
                // Don't mark message as consumed if it has been delivered but if the handler
                // succeeded.
                // E.g. We want the message to be resent when the handler fails and the service
                // is restarted at a later time.
                LOG.info("Ack {}", deliveryTag);
                channel.basicAck(deliveryTag, false);
                LOG.info("Message acknowledged.");
            } catch (Exception ex) {
                // If we don't ack the message it will be redelivered to the queue as soon as
                // this channel/connection closes.
                // See: https://www.rabbitmq.com/confirms.html#automatic-requeueing
                LOG.error(String.format("Handler %s failed.", handler.getHandler().getClass().getSimpleName()), ex);
            }
        }
    }

    private final class TypedMessageHandler {
        /**
         * Deserializes a specific subclass of a message received over the bus.
         */
        private final Function<String, Message> deserializer;
        private final MessageHandler handler;

        public TypedMessageHandler(final Function<String, Message> deserializer, final MessageHandler handler) {
            this.deserializer = deserializer;
            this.handler = handler;
        }

        public Function<String, Message> getDeserializer() {
            return deserializer;
        }

        public MessageHandler getHandler() {
            return handler;
        }
    }
}
