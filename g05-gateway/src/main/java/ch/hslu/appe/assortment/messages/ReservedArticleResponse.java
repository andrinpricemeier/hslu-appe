package ch.hslu.appe.assortment.messages;

/**
 * Represents the outcome of a reservation request.
 */
public final class ReservedArticleResponse {
    private int articleNr;
    private long reservationNr;
    /**
     * Get the article that has been reserved.
     * @return the article.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the article.
     * @param articleNr the article.
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the reservation number that has been created.
     * @return the reservation number.
     */
    public long getReservationNr() {
        return reservationNr;
    }
    /**
     * Set the reservation number.
     * @param reservationNr the reservation number.
     */
    public void setReservationNr(long reservationNr) {
        this.reservationNr = reservationNr;
    }    
}