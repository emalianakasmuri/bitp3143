/*
 * Lab 04, Exercise 6: CounterDisplay.java
 *
 * Program description:
 *   Plays the "Now serving" display above the Kopi Corner counters. It
 *   receives ready-order announcements as UDP datagrams on port 9100. Each
 *   datagram is "<seq>,<message>". The display shows only announcements
 *   newer than the last one shown, so a late or repeated datagram never
 *   sends the screen backwards. It ends on "END", or after 10 seconds with
 *   no datagram.
 *
 * What it shows:
 *   DatagramSocket and DatagramPacket on the receiving side, a receive
 *   timeout, and using a sequence number to cope with reordering and
 *   duplicates.
 *
 * How to run:
 *   Start this program first, then run ReadyAnnouncer.
 */
package lab04.kopicorner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class CounterDisplay {

    /** Port number on which the display receives datagrams. */
    private static final int PORT = 9100;

    /** Largest datagram the display expects, in bytes. */
    private static final int MAX_SIZE = 512;

    /** The display goes idle after this long with no datagram, in milliseconds. */
    private static final int IDLE_MS = 10_000;

    /**
     * Receives announcements and shows the newest ones.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            socket.setSoTimeout(IDLE_MS);
            System.out.println("[Display] Listening on port " + PORT);
            byte[] buffer = new byte[MAX_SIZE];
            int lastShown = 0;                                  // sequence number now on the screen

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);                         // one datagram = one whole message
                String text = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                String[] parts = text.split(",", 2);
                if (parts[1].equals("END")) {
                    break;
                }
                int seq = Integer.parseInt(parts[0]);
                // TODO 4: Show the announcement only if seq is greater than lastShown, and then
                //         update lastShown. Otherwise print
                //         "[Display]   (ignored seq <seq>: stale or duplicate)" and leave the screen as it is.
                lastShown = seq;
                System.out.println("[Display] NOW SERVING  " + parts[1] + "   (seq " + seq + ")");
            }
            System.out.println("[Display] Closing for the day; last shown seq " + lastShown);
        } catch (SocketTimeoutException e) {
            System.out.println("[Display] No announcements for " + IDLE_MS / 1000 + " s: display idle");
        } catch (IOException e) {
            System.err.println("[Display] " + e.getMessage());
        }
    }
}
