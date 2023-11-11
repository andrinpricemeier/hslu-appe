package ch.hslu.appe.handlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.assertj.core.api.Assertions.assertThat;

import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.Trace;
import ch.hslu.appe.entities.Restocking;
import ch.hslu.appe.messages.RestockArticlesMessage;
import ch.hslu.appe.messages.RestockFulfilledMessage;
import ch.hslu.appe.monitoring.Monitoring;
import ch.hslu.appe.repositories.RestockingRepository;
import io.opentracing.noop.NoopTracerFactory;

final class RestockArticlesHandlerTest {
    @Test
    void testRestockPublishesMessage() throws IOException {
        final var repository = mock(RestockingRepository.class);
        final var restockings = new ArrayList<Restocking>();
        final var restocking = new Restocking();
        restocking.setArticleNr(10000);
        restocking.setAmount(2);
        restockings.add(restocking);
        when(repository.getAll()).thenReturn(restockings);
        final var message = new RestockArticlesMessage();
        message.setCorrelationId("1");
        message.setReplyTo("1");
        message.setArticles(restockings);
        final var tracer = NoopTracerFactory.create();
        final var trace = mock(Trace.class);
        when(trace.createFollowsFrom(any(), any())).thenReturn(tracer.buildSpan("anything").start());
        final var bus = mock(MessageBus.class);
        final var handler = new RestockArticlesHandler(repository, trace, bus, mock(Monitoring.class));
        handler.handle(message);
        final var routeCaptor = ArgumentCaptor.forClass(String.class);
        final var messageCaptor = ArgumentCaptor.forClass(RestockFulfilledMessage.class);
        verify(bus).publish(routeCaptor.capture(), messageCaptor.capture());
        assertThat(routeCaptor.getValue()).isEqualTo(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESTOCK_FULFILLED);
        assertThat(messageCaptor.getValue().getRestockings().size()).isEqualTo(1);
    }
}
