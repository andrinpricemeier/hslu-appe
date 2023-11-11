package ch.hslu.appe.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.messages.ArticlesOrderedMessage;
import ch.hslu.appe.messages.OrderArticlesMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleReservationFailedException;
import ch.hslu.appe.repositories.RandomInMemoryArticleRepository;
import ch.hslu.appe.stock.api.StockFactory;
import io.opentracing.noop.NoopTracerFactory;

final class OrderArticlesHandlerTest {
    @Test
    void testArticlesAreOrdered() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var reservationNr = repository.reserve(repository.getAll().get(0).getArticleNr(), 1);
        final var message = new OrderArticlesMessage();
        final var bus = mock(MessageBus.class);
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createFollowsFrom(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        message.getArticleReservations().add(reservationNr);
        final var handler = new OrderArticlesHandler(repository, trace, bus, mock(Monitoring.class));
        handler.handle(message);
        assertThat(repository.getByReservationNr(reservationNr)).isNull();
    }    
    
    @Test
    void testArticleOrderedEventIsRaised() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var reservationNr = repository.reserve(repository.getAll().get(0).getArticleNr(), 1);
        final var message = new OrderArticlesMessage();
        message.setOrderId("123");
        final var bus = mock(MessageBus.class);
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createFollowsFrom(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        message.getArticleReservations().add(reservationNr);
        final var handler = new OrderArticlesHandler(repository, trace, bus, mock(Monitoring.class));
        handler.handle(message);
        final var routeCaptor = ArgumentCaptor.forClass(String.class);
        final var messageCaptor = ArgumentCaptor.forClass(ArticlesOrderedMessage.class);
        final var messageIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(bus).publish(routeCaptor.capture(), messageCaptor.capture(), messageIdCaptor.capture());
        assertThat(routeCaptor.getValue()).isEqualTo(MessageRoutes.GLOBALASSORTMENT_ARTICLE_ORDERED);
        final var orderedMessage = messageCaptor.getValue();
        assertThat(orderedMessage.getOrderedArticles().size()).isEqualTo(1);
        assertThat(orderedMessage.getOrderedArticles().get(0).getAmount()).isEqualTo(1);
        assertThat(orderedMessage.getOrderedArticles().get(0).getArticle()).isEqualTo(repository.getAll().get(0));
        assertThat(messageIdCaptor.getValue()).isEqualTo("123");
    }
}
