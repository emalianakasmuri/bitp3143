package workedexample;

/**
 * Checks Kopi Corner order lines and KitaHub weights with Boolean logic.
 */
public class OrderCheck {

    /** Prices for menu items 1 to 5, in ringgit (RM). */
    static final double[] PRICES = {2.50, 2.80, 6.50, 5.50, 3.00};

    /**
     * Checks one order line against Rules 1 and 2.
     *
     * @param itemNo   menu choice typed at the counter tablet
     * @param quantity number of portions on this line
     * @return true when both the choice and the quantity are valid
     */
    static boolean isValidLine(int itemNo, int quantity) {
        // Short-circuit: PRICES[itemNo - 1] is read only when itemNo is in range
        return itemNo >= 1 && itemNo <= PRICES.length
                && PRICES[itemNo - 1] > 0
                && quantity >= 1 && quantity <= 10;
    }

    /**
     * Checks whether a KitaHub parcel weight is outside the accepted range.
     * De Morgan's law turns !(w > 0 && w <= 30) into w <= 0 || w > 30.
     *
     * @param weightKg parcel weight in kilograms
     * @return true when the parcel must be rejected
     */
    static boolean isRejectedWeight(double weightKg) {
        return weightKg <= 0 || weightKg > 30;
    }

    public static void main(String[] args) {
        System.out.println("Item 3 x 2  valid? " + isValidLine(3, 2));
        System.out.println("Item 7 x 1  valid? " + isValidLine(7, 1));
        System.out.println("Item 1 x 0  valid? " + isValidLine(1, 0));
        System.out.println("Item 5 x 11 valid? " + isValidLine(5, 11));
        System.out.println("12.0 kg rejected? " + isRejectedWeight(12.0));
        System.out.println("35.0 kg rejected? " + isRejectedWeight(35.0));
    }
}