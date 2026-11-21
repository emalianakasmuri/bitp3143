package lab03.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/** Exercise 3A model solution (client): sends orders typed at counter tablet T1. */
public class TabletSession {

    static final String HOST = "localhost";
    static final int PORT = 5050;
    static final String TABLET_ID = "T1";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner keyboard = new Scanner(System.in)) {

            System.out.println("Connected to kitchen. Type item,quantity (for example D02,2) or BYE.");
            while (true) {
                System.out.print("> ");
                if (!keyboard.hasNextLine()) break;
                String input = keyboard.nextLine().trim();
                if (input.equalsIgnoreCase("BYE")) {
                    out.println("BYE");
                    System.out.println("Kitchen: " + in.readLine());
                    break;
                }
                out.println(TABLET_ID + "," + input.toUpperCase());
                String reply = in.readLine();
                if (reply == null) {
                    System.out.println("Kitchen closed the connection.");
                    break;
                }
                System.out.println("Kitchen: " + reply);
            }
        } catch (ConnectException e) {
            System.out.println("Cannot reach the kitchen server. Is it running on port " + PORT + "?");
        } catch (IOException e) {
            System.out.println("Network problem: " + e.getMessage());
        }
    }
}
