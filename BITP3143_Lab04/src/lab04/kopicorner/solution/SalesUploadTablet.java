/*
 * Lab 04, Exercise 3: SalesUploadTablet.java
 *
 * Program description:
 *   At closing time, counter tablet T1 at Kopi Corner uploads the day's sales
 *   to the kitchen server in binary form: a record count, then the item code,
 *   quantity sold and unit price (in sen) of each record. The kitchen replies
 *   with the number of records it accepted and the total it calculated, and
 *   the tablet checks that total against its own.
 *
 * What it shows:
 *   DataOutputStream on a socket, typed fields, and a typed reply read with
 *   DataInputStream.
 *
 * How to run:
 *   Start SalesSummaryServer first, then run this program.
 */
package lab04.kopicorner.solution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SalesUploadTablet {

    /** Host name of the kitchen server. */
    private static final String HOST = "localhost";

    /** Port number on which the sales summary server listens. */
    private static final int PORT = 5101;

    /**
     * Uploads the day's sales and checks the kitchen's total.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String[] codes = {"D01", "D02", "N01", "N02", "T02"};
        int[] quantities = {3, 4, 2, 1, 2};
        int[] pricesSen = {250, 280, 650, 700, 450};   // prices from menu.txt, in sen

        int myTotalSen = 0;
        for (int i = 0; i < codes.length; i++) {
            myTotalSen += quantities[i] * pricesSen[i];
        }

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(
                     new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(
                     new BufferedInputStream(socket.getInputStream()))) {

            out.writeInt(codes.length);                    // how many records follow
            for (int i = 0; i < codes.length; i++) {
                out.writeUTF(codes[i]);                    // 2-byte length + 3 bytes of text
                out.writeInt(quantities[i]);               // 4 bytes
                out.writeInt(pricesSen[i]);                // 4 bytes
            }
            out.flush();
            System.out.println("[T1] Uploaded " + codes.length + " sales records in " + out.size() + " bytes");

            int accepted = in.readInt();                   // the kitchen's reply: two ints
            int kitchenTotalSen = in.readInt();
            System.out.printf("[T1] Kitchen accepted %d records, total RM%.2f%n", accepted, kitchenTotalSen / 100.0);
            System.out.printf("[T1] Tablet total RM%.2f -> %s%n", myTotalSen / 100.0,
                    kitchenTotalSen == myTotalSen ? "MATCH" : "MISMATCH, check the upload");
        } catch (IOException e) {
            System.err.println("[T1] Upload failed: " + e);
        }
    }
}
