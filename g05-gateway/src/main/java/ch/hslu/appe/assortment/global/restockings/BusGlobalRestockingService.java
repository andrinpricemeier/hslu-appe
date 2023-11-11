package ch.hslu.appe.assortment.global.restockings;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Restocking;
import ch.hslu.appe.assortment.messages.ListGlobalAssortmentRestockingsMessage;
import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Deals with restockings by communicating across the RabbitMQ bus.
 */
@Singleton
public final class BusGlobalRestockingService implements GlobalRestockingService {
    private static final Logger LOG = LoggerFactory.getLogger(BusGlobalRestockingService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusGlobalRestockingService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Restocking> getAllRestockings(Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all restockings.");
        final var msg = new ListGlobalAssortmentRestockingsMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        final String response = bus.talkSync(exchangeName, MessageRoutes.GLOBAL_ASSORTMENT_ARTICLE_RESTOCK_LIST,
                mapper.writeValueAsString(msg));
        final List<Restocking> restockings = mapper.readValue(response, new TypeReference<List<Restocking>>() {
        });
        return restockings;
    }
}
