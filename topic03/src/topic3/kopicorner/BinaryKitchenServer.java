package topic3.kopicorner;

/*
 * Program 3.3: BinaryKitchenServer.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen server. It receives one order from a counter
 *   tablet in binary form: a line count, then the item code, quantity, unit
 *   price and order time of each line. It sends each line to the drinks
 *   station or the kitchen and replies with that station. The server ends
 *   after one tablet.
 *
 * What it shows:
 *   DataInputStream and DataOutputStream (Topic 2) used on a socket.
 *   Both sides must agree on the exact order and type of every field.
 *
 * How to run:
 *   Start this program first, then run BinaryTablet (Program 3.4).
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BinaryKitchenServer {

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5001;

    /**
     * Accepts one tablet, reads its order lines and replies to each.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Kitchen] Waiting for a tablet on port " + PORT);

            try (Socket tablet = serverSocket.accept();
                 DataInputStream in = new DataInputStream(
                         new BufferedInputStream(tablet.getInputStream()));
                 DataOutputStream out = new DataOutputStream(
                         new BufferedOutputStream(tablet.getOutputStream()))) {

                // Field order agreed with the tablet: int count, then (String, int, int, long) per line
                int count = in.readInt();
                System.out.println("[Kitchen] Expecting " + count + " order lines");

                for (int i = 0; i < count; i++) {
                    String itemCode = in.readUTF();
                    int qty = in.readInt();
                    int unitPriceSen = in.readInt();
                    long orderedAt = in.readLong();

                    String station = itemCode.startsWith("K") || itemCode.startsWith("T") ? "DRINKS" : "KITCHEN";
                    System.out.printf("[Kitchen] %s x%d @ RM%.2f = RM%.2f, ordered at t=%d -> %s%n",
                            itemCode, qty, unitPriceSen / 100.0, qty * unitPriceSen / 100.0, orderedAt, station);
                    out.writeUTF(station);
                }
                out.flush();   // push all buffered replies onto the network
            }
        } catch (IOException e) {
            System.err.println("[Kitchen] I/O problem: " + e.getMessage());
        }
        System.out.println("[Kitchen] Finished");
    }
}