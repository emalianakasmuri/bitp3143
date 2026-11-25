/*
 * Lab 04, Exercise 5 (provided): StockProtocol.java
 *
 * Program description:
 *   Implements the rules of KOPI-STOCK/1.0, the protocol the Kopi Corner
 *   kitchen uses to track ingredients in the store room. It takes one request
 *   line and returns the reply. Most replies are one line; the reply to LIST
 *   is a header line that says how many lines follow, then those lines. The
 *   class knows nothing about sockets.
 *
 * What it shows:
 *   Designing and implementing an application protocol: commands, status
 *   codes, validation, and framing a multi-line reply with a count.
 */
package lab04.kopicorner.emmasolution;

import java.util.LinkedHashMap;
import java.util.Map;

public class StockProtocol {

    /** Largest amount that one USE or RESTOCK request may move. */
    private static final int MAX_AMOUNT = 50;

    /** Ingredient -> units in the store room, kept in the order they were added. */
    private final Map<String, Integer> stock = new LinkedHashMap<>();

    /**
     * Creates the protocol with the store room as it is at opening time.
     */
    public StockProtocol() {
        stock.put("eggs", 30);
        stock.put("bread", 20);
        stock.put("noodles", 15);
        stock.put("kaya", 10);
    }

    /**
     * Processes one request and produces its reply.
     *
     * @param request a line such as "USE eggs 2"
     * @return the reply; every line of it starts with a status code or an ingredient
     */
    public String handle(String request) {
        if (request == null || request.isBlank()) {
            return "400 BAD_REQUEST empty line";
        }
        String[] parts = request.trim().split("\\s+");
        String command = parts[0].toUpperCase();

        switch (command) {
            case "LEVEL":
                if (parts.length != 2) {
                    return "400 BAD_REQUEST usage: LEVEL <ingredient>";
                }
                return level(parts[1].toLowerCase());
            case "USE":
                if (parts.length != 3) {
                    return "400 BAD_REQUEST usage: USE <ingredient> <amount>";
                }
                return use(parts[1].toLowerCase(), parts[2]);
            case "RESTOCK":
                if (parts.length != 3) {
                    return "400 BAD_REQUEST usage: RESTOCK <ingredient> <amount>";
                }
                return restock(parts[1].toLowerCase(), parts[2]);
            case "LIST":
                return list();
            case "QUIT":
                return "221 BYE";
            default:
                return "400 BAD_REQUEST unknown command " + command;
        }
    }

    /**
     * Reports how much of one ingredient is left.
     *
     * @param ingredient the ingredient, in lower case
     * @return 200 with the level, or 404
     */
    private String level(String ingredient) {
        Integer units = stock.get(ingredient);
        return (units == null) ? "404 UNKNOWN " + ingredient : "200 " + ingredient + " " + units;
    }

    /**
     * Adds a delivery to the store room. Use this method as the model for use().
     *
     * @param ingredient the ingredient, in lower case
     * @param amountText the amount as typed
     * @return 200 with the new level, or a 400 or 404 error
     */
    private String restock(String ingredient, String amountText) {
        if (!stock.containsKey(ingredient)) {
            return "404 UNKNOWN " + ingredient;
        }
        int amount = parseAmount(amountText);
        if (amount < 0) {
            return "400 BAD_REQUEST amount must be a whole number from 1 to " + MAX_AMOUNT;
        }
        int now = stock.get(ingredient) + amount;
        stock.put(ingredient, now);
        return "200 RESTOCKED " + ingredient + " " + now;
    }

    /**
     * Takes an amount of an ingredient out of the store room for the kitchen.
     *
     * @param ingredient the ingredient, in lower case
     * @param amountText the amount as typed
     * @return 200 with what is left, or a 400, 404 or 409 error
     */
    private String use(String ingredient, String amountText) {
        if (!stock.containsKey(ingredient)) {
            return "404 UNKNOWN " + ingredient;
        }
        int amount = parseAmount(amountText);
        if (amount < 0) {
            return "400 BAD_REQUEST amount must be a whole number from 1 to " + MAX_AMOUNT;
        }
        int left = stock.get(ingredient);
        if (amount > left) {
            return "409 NOT_ENOUGH " + ingredient + " only " + left + " left";
        }
        stock.put(ingredient, left - amount);
        return "200 USED " + amount + " " + ingredient + " " + (left - amount) + " left";
    }

    /**
     * Lists every ingredient. The first line says how many lines follow, so
     * the client knows exactly how many more lines to read.
     *
     * @return "200 <n> lines" followed by n lines of "ingredient units"
     */
    private String list() {
        StringBuilder reply = new StringBuilder("200 " + stock.size() + " lines");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            reply.append("\n").append(entry.getKey()).append(" ").append(entry.getValue());
        }
        return reply.toString();
    }

    /**
     * Converts an amount to an int.
     *
     * @param text the amount as typed
     * @return the amount, or -1 if it is not a whole number from 1 to MAX_AMOUNT
     */
    private static int parseAmount(String text) {
        try {
            int amount = Integer.parseInt(text);
            return (amount >= 1 && amount <= MAX_AMOUNT) ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Tells the server whether a reply ends the session.
     *
     * @param reply a reply produced by handle()
     * @return true if the connection should be closed
     */
    public static boolean isFinal(String reply) {
        return reply.startsWith("221");
    }
}
