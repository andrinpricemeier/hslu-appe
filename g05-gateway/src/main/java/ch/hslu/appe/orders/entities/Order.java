package ch.hslu.appe.orders.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.hslu.appe.assortment.entities.ArticleOrder;
import ch.hslu.appe.assortment.entities.ArticleReservation;
import ch.hslu.appe.customers.entities.Customer;

/**
 * Represents an order.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Order {
    private String orderId;
    private List<ArticleOrder> localAssortmentPositions = new ArrayList<ArticleOrder>();
    private List<ArticleOrder> globalAssortmentPositions = new ArrayList<ArticleOrder>();    
    private List<ArticleReservation> localAssortmentArticleReservations = new ArrayList<>();
    private List<ArticleReservation> globalAssortmentArticleReservations = new ArrayList<>();
    private Customer customer;
    private String salesperson;
    private Date orderTime;
    private OrderStatus status;
    private double total;    
    /**
     * The total price of this order.
     * @return the total
     */
    public double getTotal() {
        return total;
    }
    /**
     * Set the total price of this order.
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }
    /**
     * Get the customer of this order.
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * Set the customer.
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }    
    /**
     * Get the id of this order.
     * @return the id.
     */
    public String getOrderId() {
        return orderId;
    }
    /**
     * Set the id of this order.
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    /**
     * Get the name of the salesperson of this order.
     * @return the salesperson.
     */
    public String getSalesperson() {
        return salesperson;
    }
    /**
     * Set the salesperson.
     * @param salesperson the salesperson.
     */
    public void setSalesperson(String salesperson) {
        this.salesperson = salesperson;
    }
    /**
     * Get the ordered articles of the local assortment.
     * @return the ordered articles.
     */
    public List<ArticleOrder> getLocalAssortmentPositions() {
        return localAssortmentPositions;
    }
    /**
     * Set the ordered articles of the local assortment.
     * @param localAssortmentPositions
     */
    public void setLocalAssortmentPositions(List<ArticleOrder> localAssortmentPositions) {
        this.localAssortmentPositions = localAssortmentPositions;
    }
    /**
     * Get the ordered articles of the global assortment.
     * @return the ordered articles.
     */
    public List<ArticleOrder> getGlobalAssortmentPositions() {
        return globalAssortmentPositions;
    }
    /**
     * Set the ordered articles of the global assortment.
     * @param globalAssortmentPositions the ordered articles.
     */
    public void setGlobalAssortmentPositions(List<ArticleOrder> globalAssortmentPositions) {
        this.globalAssortmentPositions = globalAssortmentPositions;
    }
    /**
     * Get the point in time the order has been executed.
     * @return the order time.
     */
    public Date getOrderTime() {
        return orderTime;
    }
    /**
     * Set the order time.
     * @param orderTime the order time.
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    /**
     * Get the status of the order.
     * @return the status.
     */
    public OrderStatus getStatus() {
        return status;
    }
    /**
     * Set the order status.
     * @param status the status.
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    /**
     * Get the articles reserved in the local assortment for this order.
     * This list is used to later on actually order the articles.
     * @return the reservations.
     */
    public List<ArticleReservation> getLocalAssortmentArticleReservations() {
        return localAssortmentArticleReservations;
    }
    /**
     * Set the reservations.
     * @param localAssortmentArticleReservations the reservations.
     */
    public void setLocalAssortmentArticleReservations(List<ArticleReservation> localAssortmentArticleReservations) {
        this.localAssortmentArticleReservations = localAssortmentArticleReservations;
    }
    /**
     * Get the articles reserved in the global assortment for this order.
     * This list is used to later on actually order the articles.
     * @return the reservations.
     */
    public List<ArticleReservation> getGlobalAssortmentArticleReservations() {
        return globalAssortmentArticleReservations;
    }
    /**
     * Set the reservations.
     * @param globalAssortmentArticleReservations the reservations.
     */
    public void setGlobalAssortmentArticleReservations(List<ArticleReservation> globalAssortmentArticleReservations) {
        this.globalAssortmentArticleReservations = globalAssortmentArticleReservations;
    }
}
