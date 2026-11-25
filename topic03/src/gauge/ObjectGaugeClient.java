package gauge;

/*
 * Program 3.7: ObjectGaugeClient.java
 *
 * Program description:
 *   Sends two GaugeReading objects to the server and prints each
 *   acknowledgement. It then sends a HashMap, an object the server
 *   does not expect, to show the deserialization filter at work.
 *
 * What it shows:
 *   ObjectOutputStream on a socket and the effect of a server-side filter.
 *
 * How to run:
 *   Start ObjectGaugeServer (Program 3.6) first, then run this program.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ObjectGaugeClient {

    /** Host name of the server. */
    private static final String HOST = "localhost";

    /** Port number on which the server listens. */
    private static final int PORT = 5002;

    /**
     * Sends readings as whole objects, then one unexpected object.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        GaugeReading[] readings = {
            new GaugeReading("MLK-02", 1767225600L, 3.05),
            new GaugeReading("MLK-02", 1767229200L, 3.48)
        };

        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush();                       // send our stream header before reading theirs

            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                for (GaugeReading r : readings) {
                    out.writeObject(r);        // the whole object, fields and class name
                    out.flush();
                    System.out.println("[Client] Sent " + r + " -> " + in.readObject());
                }

                // An object the server never agreed to accept
                HashMap<String, String> sneaky = new HashMap<>();
                sneaky.put("cmd", "delete-all");
                out.writeObject(sneaky);
                out.flush();
                System.out.println("[Client] Sent an unexpected HashMap");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Client] Problem: " + e);
        }
    }
}
