package kopicorner;

/**
 * Applies Kak Ros's discount rules (Rules 3 and 4, Table 1.7).
 */
public class DiscountPolicy {

    private static final double STUDENT_RATE = 0.10;
    private static final double BULK_THRESHOLD = 20.00;
    private static final double BULK_AMOUNT = 2.00;

    /**
     * Works out the student discount (Rule 3).
     *
     * @param subtotal  order subtotal before any discount, in RM
     * @param isStudent true when the customer shows a valid matric card
     * @return the discount in RM, or 0.0 when the rule does not apply
     */
    public double studentDiscount(double subtotal, boolean isStudent) {
        if (isStudent) {
            return subtotal * STUDENT_RATE;
        }
        return 0.0;
    }

    /**
     * Works out the RM2.00 discount for larger orders (Rule 4).
     *
     * @param subtotal order subtotal before any discount, in RM
     * @return 2.00 when the subtotal is RM20.00 or more, otherwise 0.0
     */
    public double bulkDiscount(double subtotal) {
        if (subtotal >= BULK_THRESHOLD) {
            return BULK_AMOUNT;
        }
        return 0.0;
    }
}