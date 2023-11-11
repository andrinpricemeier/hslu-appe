package ch.hslu.appe.messages;

/**
 * Represents a request for reserving an article.
 */
public final class ArticleReservationRequest {
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
