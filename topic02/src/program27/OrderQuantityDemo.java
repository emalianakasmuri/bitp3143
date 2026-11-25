package program27;

/*
 * Program 2.7: OrderQuantityDemo.java
 *
 * Part A: a counter tablet at Kopi Corner checks the quantity typed by
 *         the customer, using try, catch and finally.
 * Part B: the same error thrown deep inside a chain of method calls,
 *         to show how an exception travels up the call stack.
 */
public class OrderQuantityDemo {

    public static void main(String[] args) {
        System.out.println("=== Part A: checking typed quantities ===");
        String[] typed = {"2", "two", "", "-1"};
        for (String input : typed) {
            try {
                int qty = Integer.parseInt(input);
                if (qty < 1) {
                    throw new IllegalArgumentException("quantity must be at least 1");
                }
                System.out.println("\"" + input + "\" -> accepted, quantity " + qty);
            } catch (NumberFormatException e) {
                System.out.println("\"" + input + "\" -> not a number, please re-enter");
            } catch (IllegalArgumentException e) {
                System.out.println("\"" + input + "\" -> " + e.getMessage());
            } finally {
                System.out.println("   (tablet screen refreshed)");
            }
        }

        System.out.println();
        System.out.println("=== Part B: an exception travelling up ===");
        try {
            sendOrder("Teh Tarik x two");
        } catch (NumberFormatException e) {
            System.out.println("main: caught " + e.getClass().getSimpleName()
                    + " -> " + e.getMessage());
        }
        System.out.println("main: tablet is ready for the next customer");
    }

    static void sendOrder(String line) {
        System.out.println("sendOrder: start");
        try {
            int qty = readQuantity(line);
            System.out.println("sendOrder: sending " + qty + " to kitchen");  // skipped
        } finally {
            System.out.println("sendOrder: finally runs");
        }
    }

    static int readQuantity(String line) {
        System.out.println("readQuantity: start");
        String part = line.substring(line.indexOf('x') + 1).trim();
        return Integer.parseInt(part);          // throws for "two"
    }
}