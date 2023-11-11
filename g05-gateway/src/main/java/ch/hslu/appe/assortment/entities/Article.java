package ch.hslu.appe.assortment.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an article in an assortment.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Article {
    private int articleNr;
    private String description;
    private double price;
    private int stock;
    /**
     * Get the description of the article.
     * @return the description
     */
    public String getDescription() {
        return description;        
    }
    /**
     * Set the description of the article.
     * @param description the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Get the price of the article.
     * @return the price.
     */
    public double getPrice() {
        return price;
    }
    /**
     * Set the price of the article.
     * @param price the price.
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * Get the article number.
     * @return the article number.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the article number.
     * @param articleNr
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the amount of articles available, the stock.
     * @return the stock.
     */
    public int getStock() {
        return stock;
    }
    /**
     * Set the stock available.
     * @param stock the stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }    
}
