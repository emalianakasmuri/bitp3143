package lab03.general;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/** 
 * Exercise 4: Kopi Corner order logger. 
 */
public class OrderLogger {

    static final String TABLET_ID = "T1";
    static final String LOG_FILE = "orders.txt";

    /** 
     * Turns the quantity text into a number from 1 to 20, or 
     * throws InvalidOrderException. 
     */
    static int validateQuantity(String text) throws InvalidOrderException {
        // TODO 1: parse text with Integer.parseInt(text.trim()).
        //         Catch NumberFormatException and throw a new InvalidOrderException instead.
        // TODO 2: if the number is below 1 or above 20, throw InvalidOrderException.
        return 0;  // replace this line
    }

    public static void main(String[] args) {
        int saved = 0;
        try (Scanner keyboard = new Scanner(System.in);
             PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, true))) {

            while (true) {
                System.out.print("Item code (X to finish): ");
                String code = keyboard.nextLine().trim().toUpperCase();
                if (code.equals("X")) break;
                if (code.isEmpty()) {
                    System.out.println("  Rejected: item code cannot be empty.");
                    continue;
                }
                System.out.print("Quantity: ");
                String qtyText = keyboard.nextLine();

                // TODO 3: call validateQuantity(qtyText) inside try-catch.
                //         Valid: write TABLET_ID + "," + code + "," + qty to log, add 1 to saved,
                //                and print "  Saved: ...".
                //         Invalid: print "  Rejected: " + the exception's message.
            }
        } catch (IOException e) {
            System.out.println("Cannot write to " + LOG_FILE + ": " + e.getMessage());
        }
        System.out.println(saved + " order(s) saved to " + LOG_FILE + ".");
    }
}