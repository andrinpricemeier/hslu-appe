package ch.hslu.appe.notifications;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.hslu.appe.notifications.entities.Notification;
import io.opentracing.Span;

/**
 * Responsible for dealing with notifications.
 */
public interface NotificationService {
    /**
     * Retrieves all notifications.
     * @param span the span to use for request tracing.
     * @return the notifications.
     * @throws JsonProcessingException thrown when deserialization fails.
     * @throws IOException thrown when communication fails.
     * @throws InterruptedException thrown when
     */
    List<Notification> getAll(final Span span) throws JsonProcessingException, IOException, InterruptedException;
}
