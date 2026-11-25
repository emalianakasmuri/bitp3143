package topic3.kopicorner;

/*
 * Program 3.12: ResilientTablet.java
 *
 * Program description:
 *   Plays a Kopi Corner counter tablet talking to an unreliable kitchen
 *   server. It runs three scenarios: retry with exponential backoff while the
 *   server restarts, a connection reset, and a read timeout on a slow request.
 *
 * What it shows:
 *   Connect timeouts, read timeouts, exponential backoff, and how each
 *   network failure appears as a different exception.
 *
 * How to run:
 *   Keep FlakyKitchenServer (Program 3.11) in the same project and run only this
 *   program. It starts the server itself, so the timings are repeatable.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class ResilientTablet {

    /** Maximum time allowed for the TCP handshake, in milliseconds. */
    private static final int CONNECT_TIMEOUT_MS = 2000;

    /** Maximum time to wait for a reply once connected, in milliseconds. */
    private static final int READ_TIMEOUT_MS = 1500;

    /** Time at which the program started, used to print elapsed time. */
    private static final long START = System.nanoTime();

    /**
     * Starts the flaky kitchen server and runs the three scenarios in order.
     *
     * @param args not used
     * @throws InterruptedException if a backoff sleep is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        // A background thread lets one program play both tablet and kitchen; threads are covered in Topic 4
        Thread server = new Thread(new FlakyKitchenServer(2500));
        server.setDaemon(true);                    // the server thread ends when main ends
        server.start();

        System.out.println("A. Connect with retry and backoff");
        System.out.println("   reply = " + sendWithRetry("PING", 5, 500));

        System.out.println("B. Connection reset by the server");
        try {
            String reply = send("CRASH");
            log("reply = " + reply);
        } catch (SocketException e) {
            log("connection lost: " + e.getMessage());
        } catch (IOException e) {
            log("other failure: " + e);
        }

        System.out.println("C. Read timeout on a slow request");
        try {
            send("SUMMARY");
        } catch (SocketTimeoutException e) {
            log("gave up waiting: " + e.getMessage());
        } catch (IOException e) {
            log("other failure: " + e);
        }
    }

    /**
     * Sends one request line and returns one reply line, with both timeouts set.
     *
     * @param request the line to send
     * @return the reply line, or null if the server closed the connection
     * @throws IOException if connecting, writing or reading fails
     */
    private static String send(String request) throws IOException {
        try (Socket socket = new Socket()) {                 // unconnected socket, so a timeout can be given
            socket.connect(new InetSocketAddress("localhost", FlakyKitchenServer.PORT), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);            // readLine() may wait at most this long
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            out.println(request);
            return in.readLine();
        }
    }

    /**
     * Retries a request when the server refuses the connection, doubling the
     * wait after each failure. Other failures are not retried.
     *
     * @param request      the line to send
     * @param maxAttempts  the largest number of attempts allowed
     * @param firstDelayMs the wait after the first failure
     * @return the reply, or a message saying that every attempt failed
     * @throws InterruptedException if a backoff sleep is interrupted
     */
    private static String sendWithRetry(String request, int maxAttempts, long firstDelayMs)
            throws InterruptedException {
        long delay = firstDelayMs;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String reply = send(request);
                log("attempt " + attempt + " succeeded");
                return reply;
            } catch (ConnectException e) {
                // Transient: the kitchen server may be restarting, so waiting and retrying makes sense
                log("attempt " + attempt + " failed (" + e.getMessage() + "), retrying in " + delay + " ms");
                Thread.sleep(delay);
                delay *= 2;                                   // exponential backoff
            } catch (IOException e) {
                log("attempt " + attempt + " failed permanently: " + e);
                return "no reply";
            }
        }
        return "no reply after " + maxAttempts + " attempts";
    }

    /**
     * Prints a message with the time elapsed since the program started.
     *
     * @param message the text to print
     */
    private static void log(String message) {
        System.out.printf("   [t=%.1fs] %s%n", (System.nanoTime() - START) / 1e9, message);
    }
}