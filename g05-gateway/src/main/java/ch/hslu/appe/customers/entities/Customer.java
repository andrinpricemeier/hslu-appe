package ch.hslu.appe.customers.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a customer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String street;
    private String postalCode;
    private String city;
    private Boolean hasReminders;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getHasReminders() {
        return hasReminders;
    }

    public void setHasReminders(Boolean hasReminders) {
        this.hasReminders = hasReminders;
    }    
}