package ch.hslu.appe.assortment.entities;

/**
 * Represents a delivery from the global assortment to the local assortment
 */
public final class Delivery {
    private int articleNr;
    private int amount;
    /**
     * Get the article of the delivery.
     * @return the article.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the article of the delivery.
     * @param articleNr the article.
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the amount delivered.
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Set the amount delivered.
     * @param amount the amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }    
}
