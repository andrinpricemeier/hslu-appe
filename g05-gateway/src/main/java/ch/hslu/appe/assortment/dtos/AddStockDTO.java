package ch.hslu.appe.assortment.dtos;

/**
 * Increases the stock in the local assortment.
 * The article number is not necessary because that is given via URI.
 */
public final class AddStockDTO {
    private int amount;

    /**
     * Get the amount to increase the stock by.
     * E.g. the current stock is 50, the amount specified in this DTO is 20.
     * This would lead to a new stock of 70 (50 + 20).
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
