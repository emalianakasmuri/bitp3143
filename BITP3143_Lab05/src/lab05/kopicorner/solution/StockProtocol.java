package lab05.kopicorner.solution;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * StockProtocol.java (Exercise 5, provided complete)
 *
 * Program description:
 *   The rules of KOPI-STOCK/1.0, the store-room protocol you met in Lab 04,
 *   and the store room's stock. The store room opens each day with 30 eggs,
 *   20 loaves of bread, 15 packets of noodles and 10 jars of kaya.
 *   reply() turns one request line into one reply, which may span several
 *   lines for LIST.
 */
public class StockProtocol {

    /** The greeting the server sends as soon as a client connects. */
    public static final String GREETING = "220 KOPI-STOCK/1.0 store ready";

    /** Ingredient name to units in stock, kept in a fixed order. */
    private final Map<String, Integer> stock = new LinkedHashMap<>();

    /** Fills the store room with the day's opening stock. */
    public StockProtocol() {
        stock.put("eggs", 30);
        stock.put("bread", 20);
        stock.put("noodles", 15);
        stock.put("kaya", 10);
    }

    /**
     * Works out the reply to one request.
     *
     * @param request one line sent by the client
     * @return the reply text, without a final newline
     */
    public String reply(String request) {
        String[] p = request.trim().split("\\s+");
        String cmd = p[0].toUpperCase();
        switch (cmd) {
            case "LEVEL":
                if (p.length != 2) {
                    return "400 BAD_REQUEST usage: LEVEL <ingredient>";
                }
                Integer units = stock.get(p[1]);
                return units == null ? "404 UNKNOWN " + p[1] : "200 " + p[1] + " " + units;
            case "USE":
                if (p.length != 3) {
                    return "400 BAD_REQUEST usage: USE <ingredient> <amount>";
                }
                Integer left = stock.get(p[1]);
                if (left == null) {
                    return "404 UNKNOWN " + p[1];
                }
                int amount;
                try {
                    amount = Integer.parseInt(p[2]);
                } catch (NumberFormatException e) {
                    return "400 BAD_REQUEST amount must be a whole number";
                }
                if (amount > left) {
                    return "409 NOT_ENOUGH " + p[1] + " only " + left + " left";
                }
                stock.put(p[1], left - amount);
                return "200 USED " + amount + " " + p[1] + " " + (left - amount) + " left";
            case "LIST":
                StringBuilder sb = new StringBuilder("200 " + stock.size() + " lines");
                stock.forEach((k, v) -> sb.append("\n").append(k).append(" ").append(v));
                return sb.toString();
            case "QUIT":
                return "221 BYE";
            default:
                return "400 BAD_REQUEST unknown command " + p[0];
        }
    }
}
