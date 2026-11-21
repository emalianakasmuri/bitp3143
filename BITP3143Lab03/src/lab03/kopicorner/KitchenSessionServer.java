package lab03.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** Exercise 3A model solution (server): queues many orders from one tablet until BYE. */
public class KitchenSessionServer {

    static final int PORT = 5050;

    public static void main(String[] args) {
        int nextQueueNo = 101;
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Kitchen server waiting on port " + PORT + "...");
            try (Socket tablet = server.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(tablet.getInputStream()));
                 PrintWriter out = new PrintWriter(tablet.getOutputStream(), true)) {

                System.out.println("Tablet connected.");
                String line;
                while ((line = in.readLine()) != null) {      // null = tablet closed the connection
                    if (line.equalsIgnoreCase("BYE")) {
                        out.println("BYE from kitchen");
                        break;
                    }
                    String[] parts = line.split(",");
                    if (parts.length != 3) {
                        out.println("REJECTED expected tablet,item,quantity");
                        continue;
                    }
                    try {
                        OrderLogger.validateQuantity(parts[2]);  // reuse Exercise 2A
                        out.println("QUEUED #" + nextQueueNo + " " + line);
                        System.out.println("Queue #" + nextQueueNo + ": " + line);
                        nextQueueNo++;
                    } catch (InvalidOrderException e) {
                        out.println("REJECTED " + e.getMessage());
                    }
                }
            }
            System.out.println("Session ended. " + (nextQueueNo - 101) + " order(s) queued.");
        } catch (IOException e) {
            System.out.println("Kitchen server error: " + e.getMessage());
        }
    }
}
