package ch.hslu.appe.assortment.local.deliveries;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Delivery;
import ch.hslu.appe.assortment.messages.ListLocalAssortmentDeliveriesMessage;
import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Deals with deliveries by communicating across the RabbitMQ bus.
 */
@Singleton
public final class BusLocalDeliveryService implements LocalDeliveryService {
    private static final Logger LOG = LoggerFactory.getLogger(BusLocalDeliveryService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusLocalDeliveryService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Delivery> getAllDeliveries(Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all deliveries.");
        final String spanContext = TextMapCodec.contextAsString((JaegerSpanContext) span.context());
        final var msg = new ListLocalAssortmentDeliveriesMessage();
        msg.setSpanContext(spanContext);
        final String response = bus.talkSync(exchangeName, MessageRoutes.LOCAL_ASSORTMENT_DELIVERY_LIST,
                mapper.writeValueAsString(msg));
        final List<Delivery> deliveries = mapper.readValue(response, new TypeReference<List<Delivery>>() {
        });
        return deliveries;
    }
}
