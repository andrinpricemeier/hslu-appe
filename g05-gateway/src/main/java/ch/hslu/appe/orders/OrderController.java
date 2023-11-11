package ch.hslu.appe.orders;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.orders.dtos.OrderStatusDTO;
import ch.hslu.appe.orders.entities.Order;
import ch.hslu.appe.orders.entities.OrderStatus;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * The REST API for orders.
 */
@Controller("/api/v1/orders")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final Tracer tracer = Configuration.fromEnv().getTracer();

    /**
     * Creates a new instance.
     * 
     * @param orderService the order service.
     */
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders.
     * 
     * @return the orders.
     */
    @Get("/")
    @Secured({ "Filialleiter", "Verkäufer" })
    public HttpResponse<List<Order>> getOrders() {
        Span span = null;
        try {
            span = tracer.buildSpan("get orders").start();
            LOG.info("Retrieving Orders");
            final var orders = orderService.getAll(span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(orders);
        } catch (Exception e) {
            LOG.error("Retrieving orders failed.", e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            return HttpResponse.serverError();
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }

    /**
     * Creates a new order.
     * 
     * @param order the order to create.
     * @return the http status.
     */
    @Post("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<Object> create(final Order order) {
        Span span = null;
        try {
            span = tracer.buildSpan("create order").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Creating order {}", order.getOrderId());
            orderService.createOrder(order, span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error(String.format("Creating order %s failed.", order.getOrderId()), e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            return HttpResponse.serverError();
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }

    /**
     * Changes the status of an order. Changing the status can trigger certain
     * behaviors, such as cancelling an order or executing it.
     * 
     * @param id          the order id.
     * @param orderStatus the requested order status.
     * @return the http status.
     */
    @Put("/{id}/status")
    @Secured({ "Verkäufer" })
    public HttpResponse<Object> changeStatus(final String id, final OrderStatusDTO orderStatus) {
        try {
            final var status = orderStatus.getStatus();
            if (status == OrderStatus.EXECUTING) {
                executeOrder(id);
            } else if (status == OrderStatus.CANCELLED) {
                cancelOrder(id);
            } else {
                LOG.warn("Received invalid order status: {} for order id {}. Ignoring the message.", status, id);
            }
            return HttpResponse.ok();
        } catch (Exception e) {
            return HttpResponse.serverError();
        }
    }

    private void executeOrder(final String id) throws IOException {
        Span span = null;
        try {
            span = tracer.buildSpan("execute order").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Executing order {}", id);
            orderService.executeOrder(id, span);
            span.setTag("http.status_code", 200);
        } catch (Exception e) {
            LOG.error(String.format("Executing order %s failed.", id), e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            throw e;
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }

    private void cancelOrder(final String id) throws IOException {
        Span span = null;
        try {
            span = tracer.buildSpan("cancel order").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Cancelling order {}", id);
            orderService.cancelOrder(id, span);
            span.setTag("http.status_code", 200);
            span.setTag("order.id", id);
        } catch (Exception e) {
            LOG.error(String.format("Cancelling order %s failed.", id), e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            throw e;
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }

    /**
     * Updates the order with the given id.
     * 
     * @param id    the order id.
     * @param order the udpated order.
     */
    @Put("/{id}")
    @Secured({ "Verkäufer" })
    public HttpResponse<Object> update(final String id, final Order order) {
        Span span = null;
        try {
            span = tracer.buildSpan("update order").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Updating order {}", order.getOrderId());
            orderService.updateOrder(order, span);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error(String.format("Updating order %s failed.", id), e);
            if (span != null) {
                span.setTag("http.status_code", 500);
                span.setTag("error", true);
                span.log(e.getMessage());
            }
            return HttpResponse.serverError();
        } finally {
            if (span != null) {
                span.finish();
            }
        }
    }
}
