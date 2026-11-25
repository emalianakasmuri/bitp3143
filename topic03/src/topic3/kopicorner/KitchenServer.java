package topic3.kopicorner;

/*
 * Program 3.1: KitchenServer.java
 *
 * Program description:
 *   The Kopi Corner kitchen server. It listens on port 5050 and serves one
 *   counter tablet at a time. Each order line it receives is confirmed with a
 *   new order number, until the tablet sends "BYE". It then waits for the
 *   next tablet.
 *
 * What it shows:
 *   The server side of the socket API: ServerSocket, accept(), and the same
 *   reader and writer classes used for files in Topic 2.
 *
 * How to run:
 *   Start this program first. Stop it with the red Terminate button in Eclipse.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class KitchenServer {

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5050;

    /** Next order number to give out; Kopi Corner starts each day at 101. */
    private static int nextOrderNo = 101;

    /**
     * Opens the listening socket and serves tablets one after another.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // try-with-resources closes the ServerSocket when the program ends (Topic 2)
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Kitchen] Listening on port " + PORT + " ...");

            while (true) {
                // accept() blocks until a tablet completes the three-way handshake
                Socket tablet = serverSocket.accept();
                handleTablet(tablet);
            }
        } catch (IOException e) {
            System.err.println("[Kitchen] Could not listen on port " + PORT + ": " + e.getMessage());
        }
    }

    /**
     * Talks to one connected tablet until it sends BYE or disconnects.
     *
     * @param tablet the socket returned by accept()
     */
    private static void handleTablet(Socket tablet) {
        String who = tablet.getInetAddress().getHostAddress() + ":" + tablet.getPort();
        System.out.println("[Kitchen] Tablet connected from " + who);

        // Wrap the socket's byte streams in character streams, exactly as with files in Topic 2
        try (tablet;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(tablet.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(tablet.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String line;
            // readLine() returns null when the tablet closes its side of the connection
            while ((line = in.readLine()) != null) {
                System.out.println("[Kitchen] Received: " + line);
                if (line.equalsIgnoreCase("BYE")) {
                    out.println("GOODBYE");
                    break;
                }
                out.println("ORDER #" + nextOrderNo + " RECEIVED: " + line.toUpperCase());
                nextOrderNo++;
            }
        } catch (IOException e) {
            System.err.println("[Kitchen] Problem with " + who + ": " + e.getMessage());
        }
        System.out.println("[Kitchen] Tablet " + who + " disconnected");
    }
}