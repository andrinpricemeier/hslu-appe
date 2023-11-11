package ch.hslu.appe.assortment.dtos;

/**
 * Represents a request to reserve an article via REST API.
 */
public final class ArticleReservationRequest {
    private int articleNr;
    private int amount;
    /**
     * Get the affected article.
     * @return the article number.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the affected article.
     * @param articleNr
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the amount of articles to reserve.
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Set the amount of articles to reserve.
     * @param amount the amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }    
}
