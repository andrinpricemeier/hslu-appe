package ch.hslu.appe.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.CancelArticleReservationsMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleReservationFailedException;
import ch.hslu.appe.repositories.RandomInMemoryArticleRepository;
import ch.hslu.appe.stock.api.StockFactory;
import io.opentracing.noop.NoopTracerFactory;

final class CancelArticleReservationsHandlerTest {
    @Test
    void testReservationsAreCancelled() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var reservationNr = repository.reserve(repository.getAll().get(0).getArticleNr(), 1);
        final var message = new CancelArticleReservationsMessage();
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createFollowsFrom(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        message.getReservations().add(reservationNr);
        final var handler = new CancelArticleReservationsHandler(repository, trace, mock(Monitoring.class));
        handler.handle(message);
        assertThat(repository.getByReservationNr(reservationNr)).isNull();
    }
}
