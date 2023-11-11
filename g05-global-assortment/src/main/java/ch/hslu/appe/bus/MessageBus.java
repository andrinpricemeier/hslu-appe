package ch.hslu.appe.bus;

import java.io.IOException;

import ch.hslu.appe.messages.Message;

/**
 * Represents a higher abstraction for dealing with the bus than the bus connector.
 * Mainly so that users of this interfaces don't have to deal with serialization.
 */
public interface MessageBus {
    /**
     * Starts listening to messages.
     * @throws IOException
     */
    void startListen() throws IOException;  
    /**
     * Registers a handler for the given route.
     * @param <T> the type of message that is received (used for deserialization).
     * @param route the route to listen to.
     * @param handler the handler to register.
     * @param messageClass the type of message that is received.
     * @throws IOException thrown if anything happens during the communication with the bus.
     */
    <T extends Message> void registerHandler(final String route, final MessageHandler handler, final Class<T> messageClass) throws IOException;
    /**
     * Publishes a message asynchronously.
     * @param <T> the type of message to publish.
     * @param route the route to send the message to.
     * @param message the message.
     * @throws IOException thrown when communication fails.
     */
    <T extends Message> void publish(final String route, final T message) throws IOException;
    /**
     * Publishes a message asynchronously, including a message id in the header.
     * The message id's purpose is to route the message through a load balancer.
     * @param <T> the type of message.
     * @param route the route.
     * @param message the message.
     * @param messageId the message id.
     * @throws IOException thrown when communication fails.
     */
    <T extends Message> void publish(final String route, final T message, final String messageId) throws IOException;
    /**
     * Replies to a received message.
     * @param route the route to reply to.
     * @param corrId the correlation id.
     * @param anyReplyObject the message to return.
     * @throws IOException thrown when communication fails.
     */
    void reply(final String route, final String corrId, final Object anyReplyObject) throws IOException;
}
