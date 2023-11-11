package ch.hslu.appe.assortment.entities;

/**
 * Represents an order of a specific article.
 */
public final class ArticleOrder {
    private int amount;
    private Article article;
    /**
     * Get the number of articles to order.
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Set the amount of articles to order.
     * @param amount the amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    /**
     * Get the article to order.
     * @return the article.
     */
    public Article getArticle() {
        return article;
    }
    /**
     * Set the article to order.
     * @param article the article.
     */
    public void setArticle(Article article) {
        this.article = article;
    }     
    
}
