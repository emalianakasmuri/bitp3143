/*
 * Lab 04, Exercise 5 (provided): StockTablet.java
 *
 * Program description:
 *   An interactive client for KOPI-STOCK/1.0, used by Kak Ros on counter
 *   tablet T1. It sends each line she types and prints the reply. When a
 *   reply header says "200 <n> lines", it reads exactly n more lines.
 *
 * How to run:
 *   Start StockServer first, then run this program. Try: LEVEL eggs,
 *   USE eggs 2, RESTOCK bread 10, LIST, QUIT
 */
package lab04.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StockTablet {

    /** Host name of the store-room server. */
    private static final String HOST = "localhost";

    /** Port number on which the store-room server listens. */
    private static final int PORT = 6100;

    /**
     * Runs one interactive KOPI-STOCK/1.0 session.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            System.out.println("[T1] Connected, waiting for greeting...");
            System.out.println("[T1] " + in.readLine());

            while (true) {
                System.out.print("T1> ");
                out.println(keyboard.nextLine());
                String reply = in.readLine();
                if (reply == null) {
                    System.out.println("[T1] Server closed the connection");
                    break;
                }
                System.out.println("[T1] " + reply);
                if (reply.matches("200 \\d+ lines")) {               // a multi-line reply follows
                    int more = Integer.parseInt(reply.split(" ")[1]);
                    for (int i = 0; i < more; i++) {
                        System.out.println("[T1]     " + in.readLine());
                    }
                }
                if (reply.startsWith("221")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[T1] Cannot reach the store-room server: " + e);
        }
    }
}
