package kopicorner;

/**
 * Runs the test cases from Table 1.8 through the order calculator.
 */
public class KopiCornerApp {

    public static void main(String[] args) {
        OrderCalculator calc = new OrderCalculator(new Menu(), new DiscountPolicy());

        OrderLine[] bigOrder = {new OrderLine(3, 3), new OrderLine(1, 1)};
        OrderLine[] lunchOrder = {new OrderLine(3, 2), new OrderLine(2, 2)};
        OrderLine[] coffeeOnly = {new OrderLine(1, 1)};

        System.out.printf("R1: RM%.2f%n", calc.total(bigOrder, true));
        System.out.printf("R2: RM%.2f%n", calc.total(lunchOrder, true));
        System.out.printf("R3: RM%.2f%n", calc.total(bigOrder, false));
        System.out.printf("R4: RM%.2f%n", calc.total(coffeeOnly, false));

        OrderLine badLine = new OrderLine(7, 1);
        System.out.println("Item 7 accepted? " + calc.isValidLine(badLine));
    }
}