package ch.hslu.appe.assortment.local;

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
import ch.hslu.appe.assortment.messages.IncreaseArticleStockMessage;
import ch.hslu.appe.assortment.messages.ListLocalAssortmentArticlesMessage;
import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.RabbitMqConfig;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;

/**
 * Deals with local assortment articles by communicating across the RabbitMQ bus.
 */
@Singleton
public final class BusLocalAssortmentService implements LocalAssortmentService {
    private static final Logger LOG = LoggerFactory.getLogger(BusLocalAssortmentService.class);
    private final BusConnector bus;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
    private final String exchangeName;

    public BusLocalAssortmentService(final BusConnector bus) throws IOException, TimeoutException {
        this.bus = bus;
        this.bus.connect();
        this.exchangeName = new RabbitMqConfig().getExchange();
    }

    @Override
    public List<Article> getAllArticles(final Span span) throws IOException, InterruptedException {
        LOG.info("Retrieving all articles.");
        final var msg = new ListLocalAssortmentArticlesMessage();
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext)span.context()));
        final String response = bus.talkSync(exchangeName, "localassortment.article.list",
                mapper.writeValueAsString(msg));
        final List<Article> articles = mapper.readValue(response, new TypeReference<List<Article>>() {
        });
        return articles;
    }

    @Override
    public void addStock(final int articleId, final int amount, final Span span) throws IOException, InterruptedException {
        LOG.info("Adding stock to article {} (stock: {})", articleId, amount);
        final var msg = new IncreaseArticleStockMessage();
        msg.setArticleNr(articleId);
        msg.setAmount(amount);
        msg.setSpanContext(TextMapCodec.contextAsString((JaegerSpanContext)span.context()));
        bus.talkAsync(exchangeName, "localassortment.article.stock.increase", mapper.writeValueAsString(msg));
    }
}
