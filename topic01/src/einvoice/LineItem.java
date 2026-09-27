package einvoice;

/**
 * Program 1.8a: LineItem.java
 *
 * Record description (data module):
 *   One line on an invoice: what was sold, how many, and the price of one.
 *   A record is a compact immutable data carrier (Java 16+).
 */
public record LineItem(String description, int quantity, double unitPrice) {

    /** @return quantity multiplied by unit price */
    public double subtotal() {
        return quantity * unitPrice;
    }
}