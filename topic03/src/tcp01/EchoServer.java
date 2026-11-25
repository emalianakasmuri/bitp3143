package tcp01;

/*
 * Program 3.1: EchoServer.java
 *
 * Program description:
 *   A single-threaded TCP server that listens on port 5000. For each client,
 *   it reads text lines and sends each line back in upper case, until the
 *   client sends "BYE". It then waits for the next client.
 *
 * What it shows:
 *   The server side of the socket API: ServerSocket, accept(), and the same
 *   reader and writer classes used for files in Topic 2.
 *
 * How to run:
 *   Start this program first. Stop it with the red Terminate button in Eclipse
 *   (or Ctrl+C in a terminal).
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoServer {

    /** Port number on which the server listens. */
    private static final int PORT = 5001;

    /**
     * Opens the listening socket and serves clients one after another.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // try-with-resources closes the ServerSocket when the program ends (Topic 2, Section 2.5)
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Listening on port " + PORT + " ...");

            while (true) {
                // accept() blocks until a client completes the three-way handshake
                Socket client = serverSocket.accept();
                handleClient(client);
            }
        } catch (IOException e) {
            System.err.println("[Server] Could not listen on port " + PORT + ": " + e.getMessage());
        }
    }

    /**
     * Talks to one connected client until it sends BYE or disconnects.
     *
     * @param client the socket returned by accept()
     */
    private static void handleClient(Socket client) {
        String who = client.getInetAddress().getHostAddress() + ":" + client.getPort();
        System.out.println("[Server] Connected to " + who);

        // Wrap the socket's byte streams in character streams, exactly as with files in Topic 2
        try (client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String line;
            // readLine() returns null when the client closes its side of the connection
            while ((line = in.readLine()) != null) {
                System.out.println("[Server] Received: " + line);
                if (line.equalsIgnoreCase("BYE")) {
                    out.println("GOODBYE");
                    break;
                }
                out.println("ECHO: " + line.toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("[Server] Problem with " + who + ": " + e.getMessage());
        }
        System.out.println("[Server] Connection to " + who + " closed");
    }
}