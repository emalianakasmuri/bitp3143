package topic3.kopicorner;

/*
 * Program 3.6: ObjectKitchenServer.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen server. Receives OrderLine objects from one
 *   tablet through an object stream. A deserialization filter accepts
 *   OrderLine and rejects every other class. When it rejects an object, it
 *   tells the tablet and waits for the tablet to close first, so the
 *   connection ends cleanly. The server ends after one tablet.
 *
 * What it shows:
 *   ObjectInputStream on a socket, the stream-header rule (create the output
 *   stream first), ObjectInputFilter as a defence against untrusted objects,
 *   and how to close a connection without causing a TCP reset.
 *
 * How to run:
 *   Keep OrderLine (Program 3.5) in the same project. Start this program
 *   first, then run ObjectTablet (Program 3.7).
 */
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ObjectKitchenServer {

    /** Port number on which the kitchen server listens. */
    private static final int PORT = 5002;

    /**
     * Allow only OrderLine (its String and int fields need no entry); reject everything else.
     * getName() gives the full class name, such as kopi.OrderLine when the class is in a package,
     * so the filter still matches if Eclipse puts your classes in a package.
     */
    private static final ObjectInputFilter FILTER =
            ObjectInputFilter.Config.createFilter(OrderLine.class.getName() + ";!*");

    /**
     * Accepts one tablet and reads objects until the tablet closes the stream.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Kitchen] Waiting for a tablet on port " + PORT);

            try (Socket tablet = serverSocket.accept();
                 // Create and flush the output stream FIRST, so the tablet's ObjectInputStream
                 // can read our stream header. Creating both input streams first would deadlock.
                 ObjectOutputStream out = new ObjectOutputStream(tablet.getOutputStream())) {
                out.flush();

                try (ObjectInputStream in = new ObjectInputStream(tablet.getInputStream())) {
                    in.setObjectInputFilter(FILTER);
                    int accepted = 0;

                    while (true) {
                        try {
                            Object obj = in.readObject();
                            if (obj instanceof OrderLine line) {         // pattern matching for instanceof
                                accepted++;
                                System.out.printf("[Kitchen] Object received: order #%d, %s x%d%n",
                                        line.orderNo(), line.itemName(), line.qty());
                                out.writeObject("ACK " + line.orderNo() + " " + line.itemCode());
                                out.flush();
                            }
                        } catch (InvalidClassException e) {
                            // The filter stopped a class we did not allow; the stream cannot continue
                            System.out.println("[Kitchen] REJECTED by filter: " + e.getMessage());
                            out.writeObject("REJECTED object not allowed");   // tell the tablet why
                            out.flush();
                            discardUntilClosed(tablet);                       // let the tablet close first
                            break;
                        } catch (EOFException e) {
                            System.out.println("[Kitchen] Tablet closed the stream");
                            break;
                        }
                    }
                    System.out.println("[Kitchen] Accepted " + accepted + " order line(s)");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Kitchen] Problem: " + e);
        }
    }

    /**
     * Reads and throws away any bytes still arriving, until the tablet closes
     * its side. Closing a socket while unread bytes remain makes TCP send a reset
     * (RST), which the tablet would see as "Connection reset".
     *
     * @param tablet the connected tablet
     * @throws IOException if reading fails
     */
    private static void discardUntilClosed(Socket tablet) throws IOException {
        tablet.setSoTimeout(5000);                 // never wait more than 5 seconds
        InputStream raw = tablet.getInputStream();
        byte[] buffer = new byte[1024];
        try {
            while (raw.read(buffer) != -1) {
                // discard
            }
        } catch (SocketTimeoutException e) {
            // the tablet did not close in time; close anyway
        }
    }
}