package lab05.kopicorner.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * CookScreens.java (Exercise 5, provided complete)
 *
 * Program description:
 *   Kopi Corner's kitchen is getting bigger: five screens now show the
 *   stock above five work stations. At 1 p.m. all five connect to the
 *   store-room server together. Each screen sends three LEVEL heartbeats
 *   one second apart, then QUIT. Each screen reports how long it waited
 *   for the greeting and when it finished.
 *
 * What it shows:
 *   Many clients arriving at once, and how long each one waits when the
 *   server has fewer worker threads than clients.
 *
 * How to run:
 *   Start PooledStockServer first, then run this program.
 */
public class CookScreens {

    /** Number of screens. */
    private static final int SCREENS = 5;

    /** The ingredient each screen watches, in turn. */
    private static final String[] WATCH = {"eggs", "bread", "noodles", "kaya", "eggs"};

    /**
     * Starts all screens together and waits for them.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while joining
     */
    public static void main(String[] args) throws InterruptedException {
        Thread[] screens = new Thread[SCREENS];
        for (int i = 0; i < SCREENS; i++) {
            String item = WATCH[i];
            screens[i] = new Thread(() -> runScreen(item), "screen-" + (i + 1));
        }
        for (Thread s : screens) {
            s.start();
            CafeLog.pause(50);                    // keep the arrival order predictable
        }
        for (Thread s : screens) {
            s.join();
        }
        CafeLog.say(String.format("All %d screens finished after %.1f s", SCREENS, CafeLog.seconds()));
    }

    /**
     * Connects, waits for the greeting, sends three heartbeats and quits.
     *
     * @param item the ingredient this screen watches
     */
    private static void runScreen(String item) {
        double asked = CafeLog.seconds();
        try (Socket socket = new Socket("localhost", 6200);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            in.readLine();                        // the greeting
            CafeLog.say(String.format("greeting after waiting %.1f s", CafeLog.seconds() - asked));
            for (int beat = 1; beat <= 3; beat++) {
                out.println("LEVEL " + item);
                CafeLog.say(in.readLine());
                CafeLog.pause(1000);
            }
            out.println("QUIT");
            in.readLine();
            CafeLog.say("done");
        } catch (IOException e) {
            CafeLog.say("cannot reach the store room: " + e.getMessage());
        }
    }
}
