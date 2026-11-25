package topic3.kopicorner;

/*
 * Program 3.8: KitchenProtocol.java
 *
 * Program description:
 *   Implements the rules of the KOPI/1.0 protocol for the Kopi Corner kitchen:
 *   it takes one request line and returns one reply line. It knows nothing
 *   about sockets.
 *
 * What it shows:
 *   Separating the protocol logic from the networking code (Topic 1, modular
 *   design), three-digit status codes, and validating every request before it
 *   touches the order queue (Topic 2).
 */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KitchenProtocol {

    /** Largest quantity Kak Ros accepts for one item in one order. */
    private static final int MAX_QTY = 20;

    /** Waiting orders: order number -> "qty item". A concurrent map is safe once several cooks share it later. */
    private final Map<Integer, String> waiting = new ConcurrentHashMap<>();

    /** Next order number to give out. */
    private int nextOrderNo = 101;

    /**
     * Processes one request line and produces exactly one reply line.
     *
     * @param request a line such as "ORDER 2 Kopi O"
     * @return a reply that starts with a three-digit status code
     */
    public String handle(String request) {
        if (request == null || request.isBlank()) {
            return "400 BAD_REQUEST empty line";
        }
        // Split into at most 3 parts so that an item name may contain spaces
        String[] parts = request.trim().split(" ", 3);
        String command = parts[0].toUpperCase();

        switch (command) {
            case "PING":
                return "200 PONG";
            case "ORDER":
                return order(parts);
            case "STATUS":
                if (parts.length < 2) {
                    return "400 BAD_REQUEST usage: STATUS <orderNo>";
                }
                String order = waiting.get(parseOrderNo(parts[1]));
                return (order == null) ? "404 NOT_FOUND" : "200 WAITING " + order;
            case "DONE":
                if (parts.length < 2) {
                    return "400 BAD_REQUEST usage: DONE <orderNo>";
                }
                return (waiting.remove(parseOrderNo(parts[1])) == null) ? "404 NOT_FOUND" : "200 DONE";
            case "QUIT":
                return "221 BYE";
            default:
                return "400 BAD_REQUEST unknown command " + command;
        }
    }

    /**
     * Validates and queues a new order.
     *
     * @param parts the request split into command, quantity and item
     * @return 201 with the order number, or a 400 error
     */
    private String order(String[] parts) {
        if (parts.length < 3) {
            return "400 BAD_REQUEST usage: ORDER <qty> <item>";
        }
        int qty;
        try {
            qty = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return "400 BAD_REQUEST quantity must be a whole number";
        }
        if (qty < 1 || qty > MAX_QTY) {
            return "400 BAD_REQUEST quantity must be 1 to " + MAX_QTY;
        }
        int orderNo = nextOrderNo++;
        waiting.put(orderNo, qty + " " + parts[2]);
        return "201 QUEUED " + orderNo;
    }

    /**
     * Converts text to an order number, or -1 if it is not a number.
     *
     * @param text the order number as typed
     * @return the number, or -1, which never matches an order
     */
    private static int parseOrderNo(String text) {
        try {
            return Integer.parseInt(text);
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