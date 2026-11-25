package topic3.kopicorner;

/*
 * Program 3.10: OrderTablet.java
 *
 * Program description:
 *   Plays a Kopi Corner counter tablet speaking KOPI/1.0. It runs a short,
 *   scripted session that includes valid orders and deliberate mistakes.
 *
 * What it shows:
 *   A client that reads the greeting first, then sends one request and reads
 *   one reply at a time, checking only the status code.
 *
 * How to run:
 *   Start OrderServer (Program 3.9) first, then run this program.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class OrderTablet {

    /** Host name of the kitchen server. */
    private static final String HOST = "localhost";

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 6000;

    /**
     * Runs a short scripted session against the kitchen server.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String[] script = {
            "PING",
            "ORDER 2 Kopi O",
            "ORDER 1 Nasi lemak ayam",
            "ORDER 0 Roti bakar",
            "ORDER two Teh tarik",
            "STATUS 102",
            "DONE 101",
            "STATUS 101",
            "REFUND 101",
            "QUIT"
        };

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            System.out.println("S: " + in.readLine());       // the greeting comes first
            for (String request : script) {
                out.println(request);
                String reply = in.readLine();
                System.out.println("C: " + request);
                System.out.println("S: " + reply);
                if (reply == null || reply.startsWith("221")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot reach the kitchen at " + HOST + ":" + PORT + " - " + e.getMessage());
        }
    }
}