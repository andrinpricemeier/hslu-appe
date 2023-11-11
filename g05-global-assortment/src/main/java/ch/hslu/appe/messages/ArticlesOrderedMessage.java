package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.entities.ArticleOrder;

public final class ArticlesOrderedMessage extends Message {
    private String orderId;
    private List<ArticleOrder> orderedArticles = new ArrayList<>();
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public List<ArticleOrder> getOrderedArticles() {
        return orderedArticles;
    }
    public void setOrderedArticles(List<ArticleOrder> orderedArticles) {
        this.orderedArticles = orderedArticles;
    }    
}
