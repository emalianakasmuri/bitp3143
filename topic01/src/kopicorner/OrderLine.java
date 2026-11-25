package kopicorner;

/**
 * One line on a Kopi Corner order: a menu item and how many portions.
 *
 * @param itemNo   menu number typed at the counter tablet
 * @param quantity number of portions
 */
public record OrderLine(int itemNo, int quantity) {

    /**
     * Checks Rule 2: the quantity must be from 1 to 10.
     *
     * @return true when the quantity is allowed
     */
    public boolean hasValidQuantity() {
        return quantity >= 1 && quantity <= 10;
    }
}