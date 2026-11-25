/*
 * Lab 04, Exercise 3: SalesSummaryServer.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen server at closing time. It receives one
 *   day's sales upload from counter tablet T1 in binary form, prints each
 *   record with its line total, and replies with two ints: the number of
 *   records accepted and the day's total in sen. The server ends after one
 *   upload.
 *
 * What it shows:
 *   DataInputStream on a socket. The server must read the fields in exactly
 *   the order and types the tablet wrote them.
 *
 * How to run:
 *   Start this program first, then run SalesUploadTablet.
 */
package lab04.kopicorner.emmasolution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SalesSummaryServer {

    /** Port number on which the sales summary server listens. */
    private static final int PORT = 5101;

    /**
     * Accepts one upload, totals it and replies.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        
    	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            
    		// Initial execution message, indicating server is running
    		System.out.println("[Kitchen] Waiting for the sales upload on port " + PORT);

            try (Socket tablet = serverSocket.accept();
                 DataInputStream in = new DataInputStream(
                         new BufferedInputStream(tablet.getInputStream()));
                 DataOutputStream out = new DataOutputStream(
                         new BufferedOutputStream(tablet.getOutputStream()))) {

            	// Read input from the tablet
                int count = in.readInt();
                System.out.println("[Kitchen] Expecting " + count + " sales records");
                int totalSen = 0;
                
                String itemCode = "";
                int quantity = 0;
                int unitPrice = 0;
                int subTotal = 0;

                for (int i = 0; i < count; i++) {
                    // TODO 1a: Read one record in the order the tablet wrote it: the item code
                    //          (readUTF), the quantity (readInt) and the unit price in sen (readInt).
                    //          Work out the line total in sen, add it to totalSen, and print the line as
                    //          System.out.printf("[Kitchen] %s  %2d x RM%5.2f = RM%6.2f%n", ...);
                	
                	// Read input line
                	itemCode = in.readUTF();
                	quantity = in.readInt();
                	unitPrice = in.readInt();
                	
                	// Lint to calculate the total in sen
                	subTotal = quantity * unitPrice;
                	
                	totalSen += subTotal;
                	
                	System.out.printf("[Kitchen] %s  %2d x RM%5.2f = RM%6.2f%n", itemCode,
                    		quantity, unitPrice/100.0, subTotal/100.0);
                }
                
                System.out.printf("[Kitchen] Day's total: RM%.2f%n", totalSen / 100.0);

                // TODO 1b: Reply with two ints, the number of records accepted and totalSen,
                //          then flush the stream so the reply actually leaves the buffer.
                out.writeInt(count);
                out.writeInt(totalSen);
                
                out.flush();
                
            }
        } catch (IOException e) {
        	
            System.err.println("[Kitchen] Upload problem: " + e);
        }
        System.out.println("[Kitchen] Finished");
    }
}
