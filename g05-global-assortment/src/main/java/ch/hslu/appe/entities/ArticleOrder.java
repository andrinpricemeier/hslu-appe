package ch.hslu.appe.entities;

/**
 * Represents an order for an article.
 */
public final class ArticleOrder {
    private int amount;
    private Article article;
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public Article getArticle() {
        return article;
    }
    public void setArticle(Article article) {
        this.article = article;
    }   
}
