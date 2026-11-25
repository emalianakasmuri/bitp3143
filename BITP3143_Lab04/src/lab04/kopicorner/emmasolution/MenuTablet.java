/*
 * Lab 04, Exercise 4: MenuTablet.java
 *
 * Program description:
 *   Plays counter tablet T1 at Kopi Corner when it starts up in the morning.
 *   It asks the kitchen server for today's menu and receives the whole list
 *   of MenuItem records as one object. A deserialization filter makes sure
 *   the tablet rebuilds nothing but a list of menu items.
 *
 * What it shows:
 *   ObjectInputStream with an ObjectInputFilter, the stream-header rule on
 *   the client side, and casting the object that readObject() returns.
 *
 * How to run:
 *   Start MenuServer first, then run this program.
 */
package lab04.kopicorner.emmasolution;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MenuTablet {

    /** Host name of the kitchen server. */
    private static final String HOST = "localhost";

    /** Port number on which the menu server listens. */
    private static final int PORT = 5102;

    /** Allow only an ArrayList of MenuItem records; reject everything else. */
    private static final ObjectInputFilter FILTER = ObjectInputFilter.Config.createFilter(
            "java.util.ArrayList;java.lang.Object;" + MenuItem.class.getName() + ";!*");

    /**
     * Requests the menu and prints it.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush();                                           // our stream header first

            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                in.setObjectInputFilter(FILTER);
                out.writeObject("MENU");
                out.flush();

                Object reply = in.readObject();
                if (reply instanceof List<?> items) {
                    System.out.println("[T1] Today's menu (" + items.size() + " items):");
                    for (Object o : items) {
                        MenuItem item = (MenuItem) o;
                        System.out.printf("[T1]   %s  %-18s RM%5.2f%n", item.code(), item.name(), item.priceSen() / 100.0);
                    }
                } else {
                    System.out.println("[T1] Unexpected reply: " + reply);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[T1] Cannot load the menu: " + e);
        }
    }
}
