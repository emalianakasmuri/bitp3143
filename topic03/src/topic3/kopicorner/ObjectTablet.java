package topic3.kopicorner;

/*
 * Program 3.7: ObjectTablet.java
 *
 * Program description:
 *   Plays counter tablet 1 at Kopi Corner. Sends two OrderLine objects to the
 *   kitchen server and prints each acknowledgement. It then sends a HashMap,
 *   an object the server does not expect, and prints the server's rejection
 *   to show the deserialization filter at work.
 *
 * What it shows:
 *   ObjectOutputStream on a socket and the effect of a server-side filter.
 *
 * How to run:
 *   Start ObjectKitchenServer (Program 3.6) first, then run this program.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ObjectTablet {

    /** Host name of the kitchen server. */
    private static final String HOST = "localhost";

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5002;

    /**
     * Sends order lines as whole objects, then one unexpected object.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        OrderLine[] lines = {
            new OrderLine(104, "M01", "Mee goreng", 1),
            new OrderLine(104, "T01", "Teh tarik", 2)
        };

        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush();                       // send our stream header before reading theirs

            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                for (OrderLine line : lines) {
                    out.writeObject(line);     // the whole object, fields and class name
                    out.flush();
                    Object reply = in.readObject();
                    System.out.println("[Tablet 1] Sent " + line + " -> " + reply);
                    if (reply.toString().startsWith("REJECTED")) {
                        return;                // the kitchen refused; stop sending
                    }
                }

                // An object the kitchen server never agreed to accept
                HashMap<String, String> sneaky = new HashMap<>();
                sneaky.put("cmd", "clear-all-orders");
                out.writeObject(sneaky);
                out.flush();
                System.out.println("[Tablet 1] Sent an unexpected HashMap -> " + in.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Tablet 1] Problem: " + e);
        }
    }
}