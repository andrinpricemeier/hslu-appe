package ch.hslu.appe.entities;

/**
 * Represents a reserved article.
 */
public final class ReservedArticle {
    public static int INVALID_RESERVATION = -1;
    private int articleNr;
    private long reservationNr;
    public int getArticleNr() {
        return articleNr;
    }
    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }
    public long getReservationNr() {
        return reservationNr;
    }
    public void setReservationNr(long reservationNr) {
        this.reservationNr = reservationNr;
    }    
}
