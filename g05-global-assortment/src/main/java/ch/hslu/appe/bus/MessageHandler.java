package ch.hslu.appe.bus;

import java.io.IOException;

import ch.hslu.appe.messages.Message;

/**
 * Represents a handler for generic messages sent across the bus.
 */
public interface MessageHandler {
    /**
     * Handles a message sent over the wire.
     * @param message the message. Can be cast to the actual subclass of the message.
     * @throws IOException thrown when communication with the bus fails.
     */
    void handle(final Message message) throws IOException;
}
