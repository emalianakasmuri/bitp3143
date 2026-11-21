package lab03.kopicorner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/** Exercise 2A model solution: Kopi Corner order logger. */
public class OrderLogger {

    static final String TABLET_ID = "T1";
    static final String LOG_FILE = "orders.txt";

    /** Turns the quantity text into a number from 1 to 20, or throws InvalidOrderException. */
    static int validateQuantity(String text) throws InvalidOrderException {
        int qty;
        try {
            qty = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new InvalidOrderException("quantity must be a whole number, not \"" + text + "\"");
        }
        if (qty < 1 || qty > 20) {
            throw new InvalidOrderException("quantity must be from 1 to 20, not " + qty);
        }
        return qty;
    }

    public static void main(String[] args) {
        int saved = 0;
        // true = append mode, so earlier orders stay in the file
        try (Scanner keyboard = new Scanner(System.in);
             PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, true))) {

            while (true) {
                System.out.print("Item code (X to finish): ");
                if (!keyboard.hasNextLine()) break;
                String code = keyboard.nextLine().trim().toUpperCase();
                if (code.equals("X")) break;
                if (code.isEmpty()) {
                    System.out.println("  Rejected: item code cannot be empty.");
                    continue;
                }
                System.out.print("Quantity: ");
                if (!keyboard.hasNextLine()) break;
                String qtyText = keyboard.nextLine();
                try {
                    int qty = validateQuantity(qtyText);
                    log.println(TABLET_ID + "," + code + "," + qty);
                    saved++;
                    System.out.println("  Saved: " + TABLET_ID + "," + code + "," + qty);
                } catch (InvalidOrderException e) {
                    System.out.println("  Rejected: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot write to " + LOG_FILE + ": " + e.getMessage());
        }
        System.out.println(saved + " order(s) saved to " + LOG_FILE + ".");
    }
}
