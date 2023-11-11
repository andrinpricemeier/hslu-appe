package ch.hslu.appe.bus;

/**
 * Encapsulates all routes used in the gateway.
 * Its main purpose is to make it easier to rename routes and to figure out where
 * a given route is used.
 */
public final class MessageRoutes {
    private MessageRoutes() {
    }

    /**
     * The route used to retrieve all customers.
     */
    public static final String CUSTOMER_GET_ALL = "customer.getall";
    public static final String GLOBAL_ASSORTMENT_ARTICLE_RESERVE = "globalassortment.article.reserve";
    public static final String GLOBAL_ASSORTMENT_ARTICLE_RESERVATION_CANCEL = "globalassortment.article.reservation.cancel";
    public static final String GLOBAL_ASSORTMENT_ARTICLE_RESTOCK_LIST = "globalassortment.article.restock.list";
    public static final String GLOBAL_ASSORTMENT_ARTICLE_LIST = "globalassortment.article.list";
    public static final String LOCAL_ASSORTMENT_DELIVERY_LIST = "localassortment.delivery.list";
    public static final String LOCAL_ASSORTMENT_ARTICLE_RESERVE = "localassortment.article.reserve";
    public static final String LOCAL_ASSORTMENT_ARTICLE_RESERVATION_CANCEL = "localassortment.article.reservation.cancel";
    public static final String ORDER_LIST = "order.list";
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_EXECUTED = "order.executed";
    public static final String ORDER_CANCELLED = "order.cancelled";
    public static final String ORDER_UPDATED = "order.updated";
}
