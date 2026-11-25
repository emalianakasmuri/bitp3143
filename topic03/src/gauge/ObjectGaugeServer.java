package gauge;

/*
 * Program 3.6: ObjectGaugeServer.java
 *
 * Program description:
 *   Receives GaugeReading objects from one client through an object stream.
 *   A deserialization filter accepts GaugeReading and rejects every other class.
 *   The server ends after serving one client.
 *
 * What it shows:
 *   ObjectInputStream on a socket, the stream-header rule (create the output
 *   stream first), and ObjectInputFilter as a defence against untrusted objects
 *   (Topic 2, Section 2.3).
 *
 * How to run:
 *   Compile GaugeReading (Program 3.5) with this program. Start this program
 *   first, then run ObjectGaugeClient (Program 3.7).
 */
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ObjectGaugeServer {

    /** Port number on which the server listens. */
    private static final int PORT = 5002;

    /** Allow only GaugeReading (its String and primitive fields need no entry); reject everything else. */
    private static final ObjectInputFilter FILTER =
            ObjectInputFilter.Config.createFilter("GaugeReading;!*");

    /**
     * Accepts one client and reads objects until the client closes the stream.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket client = serverSocket.accept();
             // Create and flush the output stream FIRST, so the client's ObjectInputStream
             // can read our stream header. Creating both input streams first would deadlock.
             ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            out.flush();

            try (ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
                in.setObjectInputFilter(FILTER);
                int accepted = 0;

                while (true) {
                    try {
                        Object obj = in.readObject();
                        if (obj instanceof GaugeReading r) {      // pattern matching for instanceof
                            accepted++;
                            System.out.printf("[Server] Object received: %s, %.2f m%n",
                                    r.stationId(), r.levelMetres());
                            out.writeObject("ACK " + accepted);
                            out.flush();
                        }
                    } catch (InvalidClassException e) {
                        // The filter stopped a class we did not allow; the stream cannot continue
                        System.out.println("[Server] REJECTED by filter: " + e.getMessage());
                        break;
                    } catch (EOFException e) {
                        System.out.println("[Server] Client closed the stream");
                        break;
                    }
                }
                System.out.println("[Server] Accepted " + accepted + " reading(s)");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Server] Problem: " + e);
        }
    }
}