package ch.hslu.appe.customers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.appe.customers.entities.Customer;
import ch.hslu.appe.notifications.NotificationService;
import ch.hslu.appe.notifications.entities.Notification;
import io.jaegertracing.Configuration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Represents the REST API for dealing with customer resources.
 */
@Controller("/api/v1/customers")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CustomerController {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
    private final Tracer tracer = Configuration.fromEnv().getTracer();
    private final CustomerService customerService;
    private final NotificationService notificationService;

    public CustomerController(final CustomerService customerService, final NotificationService notificationService) {
        this.customerService = customerService;
        this.notificationService = notificationService;
    }

    /**
     * Returns a list of all customers.
     * @return the list of customers.
     * @throws Exception
     */
    @Get("/")
    @Secured({ "Verkäufer" })
    public HttpResponse<List<Customer>> getAll() {
        Span span = null;
        try {
            span = tracer.buildSpan("get customers").start();
            LOG.info("Span context: {}", span.context().toString());
            LOG.info("Retrieving customers");
            final var customers = customerService.getAll(span);
            final var reminders = notificationService.getAll(span);
            connectCustomersWithReminders(customers, reminders);
            span.setTag("http.status_code", 200);
            return HttpResponse.ok(customers);
        } catch (Exception e) {
            LOG.error("Unexpected error occurred when retrieving customers.", e);
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

    private void connectCustomersWithReminders(final List<Customer> customers, final List<Notification> notifications) {
        final var customersWithReminders = buildReminderLookup(notifications);
        for (final var customer : customers) {
            if (customersWithReminders.contains(customer.getCustomerId())) {
                customer.setHasReminders(true);
            }
        }
    }

    private Set<Integer> buildReminderLookup(final List<Notification> notifications) {
        final Set<Integer> customersWithReminders = new HashSet<>();
        notifications.forEach(notification -> customersWithReminders.add(notification.getCustomer().getCustomerId()));
        return customersWithReminders;
    }
}
