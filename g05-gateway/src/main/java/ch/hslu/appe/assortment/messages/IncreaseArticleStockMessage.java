package ch.hslu.appe.assortment.messages;

import ch.hslu.appe.bus.Message;

/**
 * Sent to increase an article's stock.
 */
public final class IncreaseArticleStockMessage extends Message {
    private int articleNr;
    private int amount;
    /**
     * Get the article number.
     * @return the article number.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the article number.
     * @param articleNr the article number.
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the amount to increase the stock by.
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Set the amount to increase the stock by.
     * @param amount the amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }    
}
