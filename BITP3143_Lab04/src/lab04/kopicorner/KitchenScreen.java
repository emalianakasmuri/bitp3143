/*
 * Lab 04, Exercise 5: KitchenScreen.java
 *
 * Program description:
 *   The screen above the Kopi Corner stove shows the cooks how many eggs are
 *   left in the store room. It keeps one long-lived connection to the
 *   store-room server and sends a heartbeat request, "LEVEL eggs", every
 *   second. If a heartbeat fails, it reconnects using exponential backoff
 *   with jitter. If the server stays unreachable for 20 seconds, the screen
 *   tells the cooks to switch to paper stock cards. The screen runs for 20
 *   heartbeat rounds, then ends the session.
 *
 * What it shows:
 *   Connect and read timeouts, detecting a dead connection with a heartbeat,
 *   and reconnecting with capped exponential backoff, jitter and a deadline.
 *
 * How to run:
 *   Start StockServer first, then run this program.
 */
package lab04.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class KitchenScreen {

    /** Address of the store-room server. */
    private static final InetSocketAddress STORE = new InetSocketAddress("localhost", 6100);

    /** Number of heartbeat rounds before the screen ends the session. */
    private static final int ROUNDS = 20;

    /** Time between heartbeats, in milliseconds. */
    private static final long HEARTBEAT_MS = 1000;

    /** Longest wait for the TCP handshake, in milliseconds. */
    private static final int CONNECT_TIMEOUT_MS = 1000;

    /** Longest wait for any one reply line, in milliseconds. */
    private static final int READ_TIMEOUT_MS = 1500;

    /** Wait after the first failed attempt; doubled after each later failure. */
    private static final long FIRST_WAIT_MS = 500;

    /** The wait never grows beyond this, in milliseconds. */
    private static final long MAX_WAIT_MS = 4000;

    /** Largest random extra added to each wait, in milliseconds. */
    private static final int JITTER_MS = 250;

    /** Give up after the server has been unreachable for this long, in milliseconds. */
    private static final long GIVE_UP_MS = 20_000;

    /** Below this many eggs, the screen warns the cooks. */
    private static final int LOW_EGGS = 10;

    /** Source of the random jitter. */
    private static final Random RANDOM = new Random();

    /** Time at which the program started, for the log. */
    private static final long START = System.nanoTime();

    /**
     * Runs the heartbeat rounds, reconnecting whenever the connection is lost.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        for (int round = 1; round <= ROUNDS; round++) {
            try {
                if (socket == null) {
                    socket = connectWithBackoff();
                    if (socket == null) {
                        log("store server unreachable for " + GIVE_UP_MS / 1000
                                + " s: SWITCH TO PAPER STOCK CARDS");
                        return;
                    }
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                    log("greeting: " + in.readLine());
                }
                out.println("LEVEL eggs");                       // the heartbeat
                String reply = in.readLine();
                if (reply == null) {
                    throw new IOException("server closed the connection");
                }
                int eggs = Integer.parseInt(reply.substring(reply.lastIndexOf(' ') + 1));
                log("round " + round + ": eggs " + eggs + (eggs < LOW_EGGS ? "   LOW STOCK, tell Kak Ros" : ""));
            } catch (IOException e) {
                log("round " + round + ": lost the store server (" + e.getMessage() + ")");
                closeQuietly(socket);
                socket = null;                                   // reconnect in the next round
            }
            pause(HEARTBEAT_MS);
        }

        if (socket != null) {
            out.println("QUIT");
            closeQuietly(socket);
        }
        log("screen switched off after " + ROUNDS + " rounds");
    }

    /**
     * Connects to the store-room server, retrying with capped exponential
     * backoff and jitter until it succeeds or GIVE_UP_MS has passed.
     *
     * @return a connected socket with the read timeout set, or null if the deadline passed
     */
    private static Socket connectWithBackoff() {
        // TODO 3: Turn this single attempt into a loop that keeps trying until it connects
        //         or the deadline passes:
        //   - Record the start time with System.nanoTime() and set wait = FIRST_WAIT_MS.
        //   - After each failed attempt, work out sleep = wait + RANDOM.nextInt(JITTER_MS + 1).
        //   - If the time offline so far plus sleep would pass GIVE_UP_MS, return null.
        //   - Otherwise log "retrying in <sleep> ms", pause(sleep), and set
        //     wait = Math.min(wait * 2, MAX_WAIT_MS).
        //   Keep the log messages below so your output matches the lab sheet.
        Socket socket = new Socket();
        try {
            socket.connect(STORE, CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);
            log("connected on attempt 1");
            return socket;
        } catch (IOException e) {
            closeQuietly(socket);
            log("connect attempt 1 failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes a socket, ignoring any error; does nothing for null.
     *
     * @param socket the socket to close, or null
     */
    private static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // nothing more can be done
            }
        }
    }

    /**
     * Sleeps without forcing the caller to handle InterruptedException.
     *
     * @param millis how long to sleep, in milliseconds
     */
    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Prints a message with the time since the program started.
     *
     * @param message the text to print
     */
    private static void log(String message) {
        System.out.printf("[Screen t=%4.1fs] %s%n", (System.nanoTime() - START) / 1e9, message);
    }
}
