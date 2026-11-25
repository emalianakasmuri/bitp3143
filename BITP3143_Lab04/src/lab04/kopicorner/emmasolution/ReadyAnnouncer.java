/*
 * Lab 04, Exercise 6: ReadyAnnouncer.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen announcing ready orders to the counter
 *   display with UDP. Each announcement is one datagram, "<seq>,<message>",
 *   where seq rises by 1 for each new announcement. To imitate a busy café
 *   network, the datagrams are sent slightly out of order and one is sent
 *   twice. A final "END" datagram ends the demonstration.
 *
 * What it shows:
 *   Sending datagrams with no connection and no acknowledgement, and the
 *   reordering and duplication that UDP allows.
 *
 * How to run:
 *   Start CounterDisplay first, then run this program.
 */
package lab04.kopicorner.emmasolution;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ReadyAnnouncer {

    /** Address of the counter display: host name and port. */
    private static final InetSocketAddress DISPLAY = new InetSocketAddress("localhost", 9100);

    /**
     * Sends the announcements in the order a busy network might deliver them.
     *
     * @param args not used
     * @throws InterruptedException if the pause between datagrams is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        int[] sendOrder = {1, 2, 4, 3, 5, 5, 7, 6};        // 3 arrives after 4, 5 twice, 6 after 7

        try (DatagramSocket socket = new DatagramSocket()) {
            for (int seq : sendOrder) {
                String message = seq + ",ORDER " + (100 + seq) + " READY at counter " + (seq % 2 == 0 ? 2 : 1);
                send(socket, message);
                System.out.println("[Kitchen] Sent " + message);
                Thread.sleep(300);
            }
            send(socket, "0,END");
            System.out.println("[Kitchen] Sent 0,END");
        } catch (IOException e) {
            System.err.println("[Kitchen] " + e.getMessage());
        }
    }

    /**
     * Sends one text message as one datagram.
     *
     * @param socket  the socket to send from
     * @param message the text to send
     * @throws IOException if sending fails
     */
    private static void send(DatagramSocket socket, String message) throws IOException {
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(data, data.length, DISPLAY));
    }
}
