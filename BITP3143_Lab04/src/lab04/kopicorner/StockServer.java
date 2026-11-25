/*
 * Lab 04, Exercise 5 (provided): StockServer.java
 *
 * Program description:
 *   The Kopi Corner store-room server, speaking KOPI-STOCK/1.0 on port 6100.
 *   It greets each client, passes every request to StockProtocol and sends
 *   back the reply. It serves one client at a time: the next client is
 *   accepted only after the current one sends QUIT or disconnects.
 *
 * What it shows:
 *   A thin network layer around a protocol class. DELAY_MS lets you make the
 *   server slow on purpose in Exercise 5.
 *
 * How to run:
 *   Keep StockProtocol in the same package. Start this program first, then
 *   run StockTablet or KitchenScreen.
 */
package lab04.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class StockServer {

    /** Port number on which the store-room server listens. */
    private static final int PORT = 6100;

    /** Extra time before each reply, in milliseconds. 0 = normal; Exercise 5 changes it. */
    private static final long DELAY_MS = 0;

    /**
     * Accepts clients one after another and lets the protocol answer each request.
     *
     * @param args not used
     * @throws InterruptedException if the reply delay is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        StockProtocol protocol = new StockProtocol();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Store] KOPI-STOCK/1.0 ready on port " + PORT);
            while (true) {
                try (Socket client = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {

                    System.out.println("[Store] Client connected from port " + client.getPort());
                    out.println("220 KOPI-STOCK/1.0 store ready");
                    String request;
                    while ((request = in.readLine()) != null) {
                        String reply = protocol.handle(request);
                        Thread.sleep(DELAY_MS);
                        System.out.println("[Store] " + request + "  =>  " + reply.replace("\n", " | "));
                        out.println(reply);
                        if (StockProtocol.isFinal(reply)) {
                            break;
                        }
                    }
                    System.out.println("[Store] Client left");
                } catch (IOException e) {
                    // A problem with one client must not stop the whole server
                    System.err.println("[Store] Client error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[Store] Cannot listen on port " + PORT + ": " + e.getMessage());
        }
    }
}
