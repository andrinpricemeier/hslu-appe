package ch.hslu.appe.assortment.local.reservations;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.dtos.ArticleReservationRequest;
import ch.hslu.appe.assortment.messages.CancelArticleReservationsMessage;
import ch.hslu.appe.assortment.messages.ReservedArticleResponse;
import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

@Singleton
public final class BusLocalReservationService implements LocalReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(BusLocalReservationService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusLocalReservationService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<ReservedArticleResponse> reserveArticles(List<ArticleReservationRequest> reservationRequests, Span span)
            throws IOException, InterruptedException {
        LOG.info("Reserving articles.");
        final String response = bus.talkSync(exchangeName, MessageRoutes.LOCAL_ASSORTMENT_ARTICLE_RESERVE,
                mapper.writeValueAsString(reservationRequests));
        return mapper.readValue(response, new TypeReference<List<ReservedArticleResponse>>() {
        });
    }

    @Override
    public void cancelReservations(List<Long> reservations, Span span) throws IOException, InterruptedException {
        LOG.info("Cancelling reservations.");
        final String spanContext = TextMapCodec.contextAsString((JaegerSpanContext) span.context());
        final var msg = new CancelArticleReservationsMessage();
        msg.setReservations(reservations);
        msg.setSpanContext(spanContext);
        bus.talkAsync(exchangeName, MessageRoutes.LOCAL_ASSORTMENT_ARTICLE_RESERVATION_CANCEL, mapper.writeValueAsString(msg));
    }
}
