package ch.hslu.appe.entities;

/**
 * Represents an article.
 */
public final class Article {
    private int articleNr;
    private String description;
    private double price;
    private int stock;        
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getArticleNr() {
        return articleNr;
    }
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
