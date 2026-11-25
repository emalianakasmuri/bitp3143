package kopicorner;

/**
 * Works out order totals by combining the menu and the discount policy.
 */
public class OrderCalculator {

    private final Menu menu;
    private final DiscountPolicy policy;

    /**
     * Creates a calculator that uses the given menu and discount policy.
     *
     * @param menu   the menu to price items from
     * @param policy the discount rules to apply
     */
    public OrderCalculator(Menu menu, DiscountPolicy policy) {
        this.menu = menu;
        this.policy = policy;
    }

    /**
     * Checks one order line against Rules 1 and 2.
     *
     * @param line the order line to check
     * @return true when the line can be accepted
     */
    public boolean isValidLine(OrderLine line) {
        return menu.isValidItem(line.itemNo()) && line.hasValidQuantity();
    }

    /**
     * Adds up the price of every valid line.
     *
     * @param lines the lines on the order
     * @return the subtotal in RM
     */
    public double subtotal(OrderLine[] lines) {
        double sum = 0.0;
        for (OrderLine line : lines) {
            if (isValidLine(line)) {
                sum = sum + menu.priceOf(line.itemNo()) * line.quantity();
            }
        }
        return sum;
    }

    /**
     * Works out the amount to pay after both discounts.
     *
     * @param lines     the lines on the order
     * @param isStudent true when the customer shows a valid matric card
     * @return the total in RM
     */
    public double total(OrderLine[] lines, boolean isStudent) {
        double sub = subtotal(lines);
        return sub - policy.studentDiscount(sub, isStudent) - policy.bulkDiscount(sub);
    }
}