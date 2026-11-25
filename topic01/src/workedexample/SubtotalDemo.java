package workedexample;

/**
 * Works out the amount to pay for one Kopi Corner order.
 */
public class SubtotalDemo {

    public static void main(String[] args) {
        double[] prices = {2.50, 2.80, 6.50, 5.50, 3.00};

        // One order: 2 x Mee Goreng (item 3) and 2 x Teh Tarik (item 2)
        int[] itemNos = {3, 2};
        int[] quantities = {2, 2};

        double subtotal = 0.0;                       // sequence: start at zero
        for (int i = 0; i < itemNos.length; i++) {   // iteration: one pass per line
            double lineTotal = prices[itemNos[i] - 1] * quantities[i];
            subtotal = subtotal + lineTotal;
        }

        boolean isStudent = true;
        double total = subtotal;
        if (isStudent) {                             // selection: Rule 3
            total = subtotal - subtotal * 0.10;
        }
        System.out.printf("Amount to pay: RM%.2f%n", total);
    }
}