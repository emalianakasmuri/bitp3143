package topic3.kopicorner;

/*
 * Program 3.14: QueueUdpSender.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen server sending queue updates to the
 *   waiting-time display board. It sends six updates, one datagram each, and
 *   waits at most 500 milliseconds for each acknowledgement.
 *
 * What it shows:
 *   Sending datagrams without a connection, a receive timeout on UDP, and an
 *   application that decides not to resend stale data.
 *
 * How to run:
 *   Start DisplayUdpServer (Program 3.13) first, then run this program.
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class QueueUdpSender {

    /** Address of the display board: host name and port. */
    private static final InetSocketAddress DISPLAY = new InetSocketAddress("localhost", 9000);

    /** Longest wait for an acknowledgement, in milliseconds. */
    private static final int REPLY_TIMEOUT_MS = 500;

    /**
     * Sends a series of queue updates and reports each acknowledgement.
     *
     * @param args not used
     * @throws InterruptedException if the pause between updates is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        int[] waitingOrders = {4, 7, 11, 15, 14, 12};          // the 1 p.m. rush builds up, then eases

        // No connection is made: the socket just gets a local port from the operating system
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(REPLY_TIMEOUT_MS);
            int acknowledged = 0;

            for (int i = 0; i < waitingOrders.length; i++) {
                int waitMinutes = waitingOrders[i] * 2;         // about two minutes per waiting order
                String update = (i + 1) + ",QUEUE," + waitingOrders[i] + " orders," + waitMinutes + " min";
                byte[] data = update.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(data, data.length, DISPLAY));   // one message, one datagram

                byte[] buffer = new byte[64];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(reply);
                    acknowledged++;
                    System.out.println("[Kitchen] Sent " + update + "  ->  "
                            + new String(reply.getData(), 0, reply.getLength(), StandardCharsets.UTF_8));
                } catch (SocketTimeoutException e) {
                    // UDP gives no delivery guarantee; an old queue figure is useless, so do not resend
                    System.out.println("[Kitchen] Sent " + update + "  ->  no reply, moving on");
                }
                Thread.sleep(200);                                             // next update
            }
            System.out.println("[Kitchen] " + acknowledged + " of " + waitingOrders.length + " updates acknowledged");
        } catch (IOException e) {
            System.err.println("[Kitchen] " + e.getMessage());
        }
    }
}