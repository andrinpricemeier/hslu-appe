package ch.hslu.appe.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.entities.ReservedArticle;
import ch.hslu.appe.messages.ArticleReservationRequest;
import ch.hslu.appe.messages.ReserveArticlesMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.ArticleReservationFailedException;
import ch.hslu.appe.repositories.RandomInMemoryArticleRepository;
import ch.hslu.appe.stock.api.StockFactory;
import io.opentracing.noop.NoopTracerFactory;

final class ReserveArticlesHandlerTest {
    @Test
    void testReservingArticleWorks() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var message = new ReserveArticlesMessage();
        final var bus = mock(MessageBus.class);
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createChildOf(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        final var request = new ArticleReservationRequest();
        request.setArticleNr(repository.getAll().get(0).getArticleNr());
        request.setAmount(1);
        message.getReservations().add(request);
        final var handler = new ReserveArticlesHandler(repository, bus, trace, mock(Monitoring.class));
        handler.handle(message);
        final var replyToCaptor = ArgumentCaptor.forClass(String.class);
        final var corrIdCaptor = ArgumentCaptor.forClass(String.class);
        final var messageCaptor = ArgumentCaptor.forClass(Object.class);
        verify(bus).reply(replyToCaptor.capture(), corrIdCaptor.capture(), messageCaptor.capture());
        final var reservedArticles = (List<ReservedArticle>)messageCaptor.getValue();
        assertThat(reservedArticles.size()).isEqualTo(1);
        assertThat(reservedArticles.get(0).getReservationNr()).isGreaterThan(10000);
        assertThat(reservedArticles.get(0).getArticleNr()).isEqualTo(repository.getAll().get(0).getArticleNr());
    }

    @Test
    void testReservingArticleNotEnoughStockReturnsMinusOne() throws ArticleReservationFailedException, IOException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var message = new ReserveArticlesMessage();
        final var bus = mock(MessageBus.class);
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createChildOf(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        final var request = new ArticleReservationRequest();
        request.setArticleNr(repository.getAll().get(0).getArticleNr());
        request.setAmount(1000);
        message.getReservations().add(request);
        final var handler = new ReserveArticlesHandler(repository, bus, trace, mock(Monitoring.class));
        handler.handle(message);
        final var replyToCaptor = ArgumentCaptor.forClass(String.class);
        final var corrIdCaptor = ArgumentCaptor.forClass(String.class);
        final var messageCaptor = ArgumentCaptor.forClass(Object.class);
        verify(bus).reply(replyToCaptor.capture(), corrIdCaptor.capture(), messageCaptor.capture());
        final var reservedArticles = (List<ReservedArticle>)messageCaptor.getValue();
        assertThat(reservedArticles.size()).isEqualTo(1);
        assertThat(reservedArticles.get(0).getReservationNr()).isEqualTo(-1);
        assertThat(reservedArticles.get(0).getArticleNr()).isEqualTo(repository.getAll().get(0).getArticleNr());
    }
}
