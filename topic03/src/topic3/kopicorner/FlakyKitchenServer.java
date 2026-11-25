package topic3.kopicorner;

/*
 * Program 3.11: FlakyKitchenServer.java
 *
 * Program description:
 *   A deliberately unreliable copy of the Kopi Corner kitchen server on
 *   port 8000. It starts listening only after a delay (like a kitchen server
 *   restarting after a power cut), answers PING at once, aborts the connection
 *   on CRASH, and takes four seconds to answer SUMMARY (the end-of-day sales
 *   summary). It serves one connection at a time.
 *
 * What it shows:
 *   The three failures a client must handle: connection refused, connection
 *   reset and read timeout. ResilientTablet (Program 3.12) starts this server.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FlakyKitchenServer implements Runnable {

    /** Port number on which the server listens. */
    public static final int PORT = 8000;

    /** How long the server waits before it starts listening, in milliseconds. */
    private final long startDelayMs;

    /**
     * Creates the server.
     *
     * @param startDelayMs delay before the server starts listening
     */
    public FlakyKitchenServer(long startDelayMs) {
        this.startDelayMs = startDelayMs;
    }

    /**
     * Waits, then accepts connections and serves them one at a time.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(startDelayMs);                       // the kitchen server is "restarting"
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("    [Kitchen] now listening on port " + PORT);
                while (true) {
                    serve(serverSocket.accept());             // one connection at a time, as in Section 3.2
                }
            }
        } catch (IOException e) {
            System.err.println("    [Kitchen] " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Answers one request according to the command it contains.
     *
     * @param client the connected tablet
     * @throws InterruptedException if the slow reply is interrupted
     */
    private void serve(Socket client) throws InterruptedException {
        try (client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String request = in.readLine();
            if ("SUMMARY".equals(request)) {
                Thread.sleep(4000);                           // far longer than the tablet will wait
                out.println("SUMMARY 412 orders");
            } else if ("CRASH".equals(request)) {
                client.setSoLinger(true, 0);                  // close() now sends a TCP reset (RST)
                return;                                       // leave without replying
            } else {
                out.println("PONG");
            }
        } catch (IOException e) {
            // The tablet may already have given up; nothing more to do
        }
    }
}