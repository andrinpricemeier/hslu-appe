package ch.hslu.appe.notifications.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.hslu.appe.customers.entities.Customer;

/**
 * Represents a notification, e.g. for a reminder.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    private String notificationNr;
    private Customer customer;
    private String type;
    private String content;

    /**
     * Get the notification number.
     * @return the notification number.
     */
    public String getNotificationNr() {
        return notificationNr;
    }

    /**
     * Set the notification number.
     * @param notificationNr the notification number.
     */
    public void setNotificationNr(String notificationNr) {
        this.notificationNr = notificationNr;
    }

    /**
     * Get the customer associated with this notification.
     * @return the customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the customer associated with this notification.
     * @param customer the customer.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Get the type of this notification.
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of this notification.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the content.
     * @return the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content.
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
