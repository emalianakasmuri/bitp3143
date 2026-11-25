package tcp01;

/*
 * Program 3.2: EchoClient.java
 *
 * Program description:
 *   Connects to the echo server on localhost, port 5001, sends three messages
 *   followed by BYE, and prints every reply.
 *
 * What it shows:
 *   The client side of the socket API: new Socket(host, port), then reading
 *   and writing through the socket's streams.
 *
 * How to run:
 *   Start EchoServer (Program 3.1) first, then run this program.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {

    /** Host name of the server; "localhost" means this same machine. */
    private static final String HOST = "localhost";

    /** Port number on which the server listens. */
    private static final int PORT = 5001;

    /**
     * Sends a fixed list of messages to the server and prints each reply.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String[] messages = {"hello server", "sockets are streams", "selamat pagi", "BYE"};

        // The constructor performs the three-way handshake; it fails fast if no server is listening
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            System.out.println("[Client] Connected from local port " + socket.getLocalPort());

            for (String msg : messages) {
                out.println(msg);                 // send one line; autoflush pushes it onto the network
                String reply = in.readLine();     // block until the server's reply line arrives
                System.out.println("[Client] Sent: " + msg + "  ->  Reply: " + reply);
            }
        } catch (IOException e) {
            System.err.println("[Client] Cannot reach " + HOST + ":" + PORT + " - " + e.getMessage());
        }
    }
}