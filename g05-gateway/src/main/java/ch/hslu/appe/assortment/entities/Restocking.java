package ch.hslu.appe.assortment.entities;

/**
 * Represents a restocking request in the global assortment (a "Nachbestellung").
 */
public final class Restocking {
    private int articleNr;
    private int amount;
    /**
     * Get the article to restock.
     * @return the article.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the article to restock.
     * @param articleNr the article.
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the amount of articles to restock.
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Set the amount of articles to restock.
     * @param amount the amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
