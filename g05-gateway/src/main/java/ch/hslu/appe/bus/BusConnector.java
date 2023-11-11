package ch.hslu.appe.bus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * The extracted interface of the bus connector to make it easier to test.
 */
public interface BusConnector extends AutoCloseable {
        /**
         * Sends a message asynchronously.
         * 
         * @param exchange the exchange.
         * @param route    the route.
         * @param message  the message in JSON format.
         * @throws IOException thrown when anything goes wrong.
         */
        void talkAsync(final String exchange, final String route, final String message) throws IOException;

        /**
         * Sends a message asynchronously and sets the message id header. The message id
         * is used to route orders to the correct order service.
         * 
         * @param exchange  the exchange.
         * @param route     the route.
         * @param message   the message.
         * @param messageId the message id.
         * @throws IOException thrown when anything goes wrong.
         */
        void talkAsync(final String exchange, final String route, final String message, final String messageId)
                        throws IOException;

        /**
         * Sends a message synchronously.
         * 
         * @param exchange the exchange.
         * @param route    the route.
         * @param message  the message.
         * @return the response.
         * @throws IOException          thrown when anything goes wrong.
         * @throws InterruptedException thrown when the thread is interrupted.
         */
        String talkSync(final String exchange, final String route, final String message)
                        throws IOException, InterruptedException;

        /**
         * Connects to the bus.
         * 
         * @throws IOException          thrown when anything goes wrong.
         * @throws InterruptedException thrown when the thread is interrupted.
         */
        void connect() throws IOException, TimeoutException;

        /**
         * Closes the connection.
         */
        void close();
}
