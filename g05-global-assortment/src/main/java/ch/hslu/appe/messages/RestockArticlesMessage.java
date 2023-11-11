package ch.hslu.appe.messages;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.entities.Restocking;

public final class RestockArticlesMessage extends Message {
    private List<Restocking> articles = new ArrayList<>();

    public List<Restocking> getArticles() {
        return articles;
    }

    public void setArticles(List<Restocking> articles) {
        this.articles = articles;
    }    
}
