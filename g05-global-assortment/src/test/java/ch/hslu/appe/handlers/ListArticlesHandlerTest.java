package ch.hslu.appe.handlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.ListArticlesMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleReservationFailedException;
import ch.hslu.appe.repositories.RandomInMemoryArticleRepository;
import ch.hslu.appe.stock.api.StockFactory;
import io.opentracing.noop.NoopTracerFactory;

final class ListArticlesHandlerTest {
    @Test
    void testArticlesAreReturned() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var message = new ListArticlesMessage();
        message.setCorrelationId("1");
        message.setReplyTo("1");
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createChildOf(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        final var bus = mock(MessageBus.class);
        final var handler = new ListArticlesHandler(repository, bus, trace, mock(Monitoring.class));
        handler.handle(message);
        verify(bus).reply(message.getReplyTo(), message.getCorrelationId(), repository.getAll());
    }
}
