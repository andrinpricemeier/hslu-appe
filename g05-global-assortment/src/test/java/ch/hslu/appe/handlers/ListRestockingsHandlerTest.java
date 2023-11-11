package ch.hslu.appe.handlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.entities.Restocking;
import ch.hslu.appe.messages.ListRestockingsMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.RestockingRepository;
import io.opentracing.noop.NoopTracerFactory;

final class ListRestockingsHandlerTest {
    @Test
    void testRestockingsAreReturned() throws IOException {
        final var repository = mock(RestockingRepository.class);
        final var restockings = new ArrayList<Restocking>();
        when(repository.getAll()).thenReturn(restockings);
        final var message = new ListRestockingsMessage();
        message.setCorrelationId("1");
        message.setReplyTo("1");
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createChildOf(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        final var bus = mock(MessageBus.class);
        final var handler = new ListRestockingsHandler(repository, bus, trace, mock(Monitoring.class));
        handler.handle(message);
        verify(bus).reply(message.getReplyTo(), message.getCorrelationId(), repository.getAll());
    }
}
