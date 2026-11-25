package topic3.kopicorner;

/*
 * Program 3.4: BinaryTablet.java
 *
 * Program description:
 *   Plays counter tablet 1 at Kopi Corner. Sends one three-line order to the
 *   kitchen server in binary form and prints the station returned for each line.
 *
 * What it shows:
 *   Writing typed fields with DataOutputStream, counting the bytes sent,
 *   and reading typed replies with DataInputStream.
 *
 * How to run:
 *   Start BinaryKitchenServer (Program 3.3) first, then run this program.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BinaryTablet {

    /** Host name of the kitchen server. */
    private static final String HOST = "localhost";

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5001;

    /**
     * Sends one order as binary fields and prints the kitchen's replies.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String[] codes = {"K01", "N01", "R01"};
        int[] quantities = {2, 2, 1};
        int[] pricesSen = {200, 850, 300};          // prices in sen avoid rounding errors
        long orderedAt = 1767243600L;               // seconds since 1970-01-01 UTC

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(
                     new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(
                     new BufferedInputStream(socket.getInputStream()))) {

            out.writeInt(codes.length);                  // how many order lines follow
            for (int i = 0; i < codes.length; i++) {
                out.writeUTF(codes[i]);                  // 2-byte length + 3 bytes of text
                out.writeInt(quantities[i]);             // always 4 bytes
                out.writeInt(pricesSen[i]);              // always 4 bytes
                out.writeLong(orderedAt);                // always 8 bytes
            }
            out.flush();                                  // nothing leaves the buffer until here
            System.out.println("[Tablet 1] Sent " + out.size() + " bytes for " + codes.length + " order lines");

            for (String code : codes) {
                System.out.println("[Tablet 1] " + code + " -> " + in.readUTF());
            }
        } catch (IOException e) {
            System.err.println("[Tablet 1] Cannot reach " + HOST + ":" + PORT + " - " + e.getMessage());
        }
    }
}
