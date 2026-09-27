package einvoice;

/**
 * This record represent a data module that described one line of data for
 * an invoice: what was sold, how many, and the price of one.
 *
 * A record is a compact immutable data carrier (Java 16+).
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development 
 */
public record LineItem(String description, int quantity, double unitPrice) {

    /** @return quantity multiplied by unit price */
    public double calcuateSubtotal() {
        return quantity * unitPrice;
    }
}