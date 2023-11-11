package ch.hslu.appe.assortment.entities;
/**
 * Represents a reserved article.
 */
public final class ArticleReservation {
    private int articleNr;
    private long reservationNr;
    /**
     * Get the reserved article's number.
     * @return the article number.
     */
    public int getArticleNr() {
        return articleNr;
    }
    /**
     * Set the reserved article's number.
     * @param articleNr
     */
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    /**
     * Get the reservation number for ordering the article later on.
     * @return the reservation number.
     */
    public long getReservationNr() {
        return reservationNr;
    }
    /**
     * Set the reservation number for ordering the article later on.
     * @param reservationNr the reservation number.
     */
    public void setReservationNr(long reservationNr) {
        this.reservationNr = reservationNr;
    }    
}