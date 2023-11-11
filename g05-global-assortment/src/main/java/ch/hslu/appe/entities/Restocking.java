package ch.hslu.appe.entities;

/**
 * Represents a restocking (a "Nachbestellung").
 */
public final class Restocking {
    private int articleNr;
    private int amount;
    public int getArticleNr() {
        return articleNr;
    }
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }    
}
