package kopicorner;

/**
 * Holds the Kopi Corner menu (Table 1.1) and answers questions about it.
 */
public class Menu {

    private static final String[] NAMES =
            {"Kopi", "Teh Tarik", "Mee Goreng", "Maggi Goreng", "Roti Bakar"};
    private static final double[] PRICES = {2.50, 2.80, 6.50, 5.50, 3.00};

    /**
     * Checks Rule 1: the menu choice must exist.
     *
     * @param itemNo menu number from 1 to 5
     * @return true when the item is on the menu
     */
    public boolean isValidItem(int itemNo) {
        return itemNo >= 1 && itemNo <= PRICES.length;
    }

    /**
     * Looks up the price of a menu item.
     *
     * @param itemNo a valid menu number
     * @return the price in RM
     */
    public double priceOf(int itemNo) {
        return PRICES[itemNo - 1];
    }

    /**
     * Looks up the name of a menu item.
     *
     * @param itemNo a valid menu number
     * @return the item name shown on the bill
     */
    public String nameOf(int itemNo) {
        return NAMES[itemNo - 1];
    }
}