/*
 * Copyright 2020 Roland Christen, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.appe.bus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Beispielcode für Verbindung mit RabbitMQ.
 */
@Singleton
public final class RabbitMQBusConnector implements BusConnector {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQBusConnector.class);
    private boolean isConnected;
    // connection to bus
    private Connection connection;

    // use different channels for different threads
    private Channel channelTalk;
    private Channel channelListen;

    public void talkAsync(final String exchange, final String route, final String message) throws IOException {
        talkAsync(exchange, route, message, null);
    }

    public void talkAsync(final String exchange, final String route, final String message, final String messageId)
            throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties();
        if (messageId != null) {
            props = props.builder().messageId(messageId).build();
        }
        channelTalk.basicPublish(exchange, route, props, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Beispiel für synchrone Kommunikation.
     *
     * @param exchange Exchange.
     * @param route    Route.
     * @param message  Message.
     * @return String.
     * @throws IOException          Exception.
     * @throws InterruptedException Exception.
     */
    public String talkSync(final String exchange, final String route, final String message)
            throws IOException, InterruptedException {

        // create a temporary reply queue
        final String corrId = UUID.randomUUID().toString();
        final String replyQueueName = channelTalk.queueDeclare().getQueue();
        channelTalk.queueBind(replyQueueName, exchange, replyQueueName);

        // setup receiver
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        final String consumerId = channelTalk.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {

            // check if response matches correlation id
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
            // empty
        });

        // send message
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
                .build();
        channelTalk.basicPublish(exchange, route, props, message.getBytes(StandardCharsets.UTF_8));

        // To receive a message without timeout, use:
        // String result = response.take();

        // receive message with timeout
        final String result = response.poll(20, TimeUnit.SECONDS);
        channelTalk.basicCancel(consumerId);
        return result;

    }

    /**
     * Öffnet die Verbindung zu RabbitMQ.
     *
     * @throws IOException      IOException.
     * @throws TimeoutException TimeoutException.
     */
    public void connect() throws IOException, TimeoutException {
        if (isConnected) {
            return;
        }
        RabbitMqConfig config = new RabbitMqConfig();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        LOG.debug("Connecting to {}...", config.getHost());
        this.connection = factory.newConnection();
        this.channelTalk = connection.createChannel();
        this.channelListen = connection.createChannel();
        LOG.debug("Successfully connected to {}...", config.getHost());
        isConnected = true;
    }

    /**
     * Schliesst die Verbindung zu RabbitMQ.
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        try {
            channelTalk.close();
            channelListen.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            LOG.error("Closing bus failed.", e);
        }
    }
}
