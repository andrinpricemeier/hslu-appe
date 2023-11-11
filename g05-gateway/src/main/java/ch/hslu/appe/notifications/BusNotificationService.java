package ch.hslu.appe.notifications;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.notifications.entities.Notification;
import ch.hslu.appe.notifications.messages.NotificationListMessage;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Responsible for dealing with notifications by communicating over the RabbitMQ bus.
 */
@Singleton
public final class BusNotificationService implements NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(BusNotificationService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String exchangeName;

    public BusNotificationService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Notification> getAll(final Span span) throws JsonProcessingException, IOException, InterruptedException {
        LOG.info("Retrieving all notifications.");
        final var msg = new NotificationListMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext)span.context()));
        final String response = bus.talkSync(exchangeName, "notification.list", mapper.writeValueAsString(msg));
        return mapper.readValue(response, new TypeReference<List<Notification>>(){});
    }
}
