package topic3.kopicorner;

/*
 * Program 3.13: DisplayUdpServer.java
 *
 * Program description:
 *   Plays the waiting-time display board at the Kopi Corner entrance. It
 *   receives queue updates from the kitchen server as UDP datagrams on port
 *   9000 and acknowledges each one. To make packet loss visible, it ignores
 *   every third datagram on purpose. It stops after six datagrams.
 *
 * What it shows:
 *   DatagramSocket and DatagramPacket: no connection, no accept(), and one
 *   message per datagram.
 *
 * How to run:
 *   Start this program first, then run QueueUdpSender (Program 3.14).
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class DisplayUdpServer {

    /** Port number on which the display board receives datagrams. */
    private static final int PORT = 9000;

    /** Largest datagram this display expects, in bytes. */
    private static final int MAX_SIZE = 512;

    /** Number of datagrams to receive before the demonstration ends. */
    private static final int EXPECTED = 6;

    /**
     * Receives queue updates and acknowledges most of them.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Binding to a port is all a UDP server needs: there is no listen or accept step
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("[Display] Waiting for queue updates on port " + PORT);
            byte[] buffer = new byte[MAX_SIZE];

            for (int count = 1; count <= EXPECTED; count++) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);                       // blocks until one datagram arrives

                // Each datagram carries exactly one message: its boundaries are preserved
                String text = new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8);
                String seq = text.split(",")[0];

                if (count % 3 == 0) {
                    System.out.println("[Display] " + text + "  (dropped on purpose, no reply)");
                    continue;                                   // simulate a lost reply
                }
                System.out.println("[Display] " + text);

                // The reply goes back to the address and port stored in the request
                byte[] reply = ("ACK " + seq).getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(reply, reply.length, request.getSocketAddress()));
            }
        } catch (IOException e) {
            System.err.println("[Display] " + e.getMessage());
        }
        System.out.println("[Display] Finished");
    }
}