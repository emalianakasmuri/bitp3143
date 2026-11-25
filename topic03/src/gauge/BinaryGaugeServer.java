package gauge;

/*
 * Program 3.3: BinaryGaugeServer.java
 *
 * Program description:
 *   Receives river-level readings from one telemetry client in binary form,
 *   classifies each reading and sends the status back. The server ends after
 *   serving one client.
 *
 * What it shows:
 *   DataInputStream and DataOutputStream (Topic 2, Section 2.3) used on a socket.
 *   Both sides must agree on the exact order and type of every field.
 *
 * How to run:
 *   Start this program first, then run BinaryGaugeClient (Program 3.4).
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BinaryGaugeServer {

    /** Port number on which the server listens. */
    private static final int PORT = 5001;

    /**
     * Accepts one client, reads its readings and replies with a status for each.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Waiting for a telemetry station on port " + PORT);

            try (Socket station = serverSocket.accept();
                 DataInputStream in = new DataInputStream(
                         new BufferedInputStream(station.getInputStream()));
                 DataOutputStream out = new DataOutputStream(
                         new BufferedOutputStream(station.getOutputStream()))) {

                // Field order agreed with the client: int count, then (String, long, double) per reading
                int count = in.readInt();
                System.out.println("[Server] Expecting " + count + " readings");

                for (int i = 0; i < count; i++) {
                    String stationId = in.readUTF();
                    long epochSecond = in.readLong();
                    double levelMetres = in.readDouble();

                    String status = classify(levelMetres);
                    System.out.printf("[Server] %s at t=%d: %.2f m -> %s%n",
                            stationId, epochSecond, levelMetres, status);
                    out.writeUTF(status);
                }
                out.flush();   // push all buffered replies onto the network
            }
        } catch (IOException e) {
            System.err.println("[Server] I/O problem: " + e.getMessage());
        }
        System.out.println("[Server] Finished");
    }

    /**
     * Maps a water level to a flood status (illustrative thresholds only).
     *
     * @param level water level in metres
     * @return Normal, Alert, Warning or Danger
     */
    private static String classify(double level) {
        if (level >= 4.0) {
            return "DANGER";
        } else if (level >= 3.5) {
            return "WARNING";
        } else if (level >= 3.0) {
            return "ALERT";
        }
        return "NORMAL";
    }
}