package topic3.kopicorner;

/*
 * Program 3.9: OrderServer.java
 *
 * Program description:
 *   The Kopi Corner kitchen server, now speaking KOPI/1.0 on port 6000. It
 *   greets each tablet, passes every request to KitchenProtocol and sends back
 *   the reply. Tablets are served one at a time.
 *
 * What it shows:
 *   A thin network layer around a protocol class, and a server loop that
 *   survives errors with any single client.
 *
 * How to run:
 *   Keep KitchenProtocol (Program 3.8) in the same project. Start this program
 *   first, then run OrderTablet (Program 3.10).
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class OrderServer {

    /** Port number on which the server listens. */
    private static final int PORT = 6000;

    /**
     * Accepts tablets in a loop and lets the protocol object answer each request.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        KitchenProtocol protocol = new KitchenProtocol();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[OrderServer] KOPI/1.0 ready on port " + PORT);
            while (true) {
                try (Socket tablet = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(tablet.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter out = new PrintWriter(tablet.getOutputStream(), true, StandardCharsets.UTF_8)) {

                    out.println("220 KOPI/1.0 kitchen ready");   // greeting line, sent first by the server
                    String request;
                    while ((request = in.readLine()) != null) {
                        String reply = protocol.handle(request);
                        System.out.println("[OrderServer] " + request + "  =>  " + reply);
                        out.println(reply);
                        if (KitchenProtocol.isFinal(reply)) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    // A problem with one tablet must not stop the whole kitchen server
                    System.err.println("[OrderServer] Tablet error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[OrderServer] Cannot listen on port " + PORT + ": " + e.getMessage());
        }
    }
}