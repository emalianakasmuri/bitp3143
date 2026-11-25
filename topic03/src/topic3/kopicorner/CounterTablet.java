package topic3.kopicorner;

/*
 * Program 3.2: CounterTablet.java
 *
 * Program description:
 *   Plays counter tablet 1 at Kopi Corner. Connects to the kitchen server on
 *   localhost, port 5050, sends three orders followed by BYE, and prints every
 *   confirmation.
 *
 * What it shows:
 *   The client side of the socket API: new Socket(host, port), then reading
 *   and writing through the socket's streams.
 *
 * How to run:
 *   Start KitchenServer (Program 3.1) first, then run this program.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CounterTablet {

    /** Host name of the kitchen server; "localhost" means this same machine. */
    private static final String HOST = "localhost";

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5050;

    /**
     * Sends a fixed list of orders to the kitchen and prints each confirmation.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String[] orders = {"Kopi O x2", "Roti bakar x1", "Mee goreng x1", "BYE"};

        // The constructor performs the three-way handshake; it fails fast if no server is listening
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            System.out.println("[Tablet 1] Connected from local port " + socket.getLocalPort());

            for (String order : orders) {
                out.println(order);                 // send one line; autoflush pushes it onto the network
                String reply = in.readLine();       // block until the kitchen's reply line arrives
                System.out.println("[Tablet 1] Sent: " + order + "  ->  Reply: " + reply);
            }
        } catch (IOException e) {
            System.err.println("[Tablet 1] Cannot reach the kitchen at " + HOST + ":" + PORT + " - " + e.getMessage());
        }
    }
}
