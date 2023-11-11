package ch.hslu.appe.assortment.global;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.assortment.entities.Article;
import ch.hslu.appe.assortment.messages.ListGlobalAssortmentArticlesMessage;
import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Deals with the articles in the global assortment by communicating with the
 * RabbitMQ bus.
 */
@Singleton
public final class BusGlobalAssortmentService implements GlobalAssortmentService {
    private static final Logger LOG = LoggerFactory.getLogger(BusGlobalAssortmentService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusGlobalAssortmentService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Article> getAllArticles(final Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all articles.");
        final var msg = new ListGlobalAssortmentArticlesMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext) span.context()));
        final String response = bus.talkSync(exchangeName, MessageRoutes.GLOBAL_ASSORTMENT_ARTICLE_LIST,
                mapper.writeValueAsString(msg));
        final List<Article> articles = mapper.readValue(response, new TypeReference<List<Article>>() {
        });
        return articles;
    }
}
