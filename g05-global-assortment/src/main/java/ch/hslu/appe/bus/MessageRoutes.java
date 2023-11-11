package ch.hslu.appe.bus;

/**
 * Represents routes used in this service.
 * They rarely if ever change that's why they are not in the properties file.
 * In this way if a route changes it is easy to find all usages.
 */
public final class MessageRoutes {
    private MessageRoutes() {
    }
    public static final String GLOBALASSORTMENT_ARTICLE_LIST = "globalassortment.article.list";
    public static final String GLOBALASSORTMENT_ARTICLE_RESERVE = "globalassortment.article.reserve";
    public static final String GLOBALASSORTMENT_ARTICLE_ORDER = "globalassortment.article.order";
    public static final String GLOBALASSORTMENT_ARTICLE_ORDERED = "globalassortment.article.ordered";
    public static final String GLOBALASSORTMENT_ARTICLE_RESERVATION_CANCEL = "globalassortment.article.reservation.cancel";
    public static final String GLOBALASSORTMENT_ARTICLE_RESTOCK = "globalassortment.article.restock";
    public static final String GLOBALASSORTMENT_ARTICLE_RESTOCK_LIST = "globalassortment.article.restock.list";
    public static final String GLOBALASSORTMENT_ARTICLE_RESTOCK_FULFILLED = "globalassortment.article.restock.fulfilled";
}
