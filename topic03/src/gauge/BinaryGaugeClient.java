package gauge;

/*
 * Program 3.4: BinaryGaugeClient.java
 *
 * Program description:
 *   Acts as a river telemetry station. Sends three readings to the server in
 *   binary form and prints the status returned for each one.
 *
 * What it shows:
 *   Writing typed fields with DataOutputStream, counting the bytes sent,
 *   and reading typed replies with DataInputStream.
 *
 * How to run:
 *   Start BinaryGaugeServer (Program 3.3) first, then run this program.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BinaryGaugeClient {

    /** Host name of the server. */
    private static final String HOST = "localhost";

    /** Port number on which the server listens. */
    private static final int PORT = 5001;

    /**
     * Sends a batch of readings and prints the server's verdicts.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String stationId = "MLK-01";
        long[] times = {1767225600L, 1767229200L, 1767232800L};
        double[] levels = {2.85, 3.62, 4.10};

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(
                     new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(
                     new BufferedInputStream(socket.getInputStream()))) {

            out.writeInt(levels.length);                // how many readings follow
            for (int i = 0; i < levels.length; i++) {
                out.writeUTF(stationId);                 // 2-byte length + 6 bytes of text
                out.writeLong(times[i]);                 // always 8 bytes
                out.writeDouble(levels[i]);              // always 8 bytes
            }
            out.flush();                                  // nothing leaves the buffer until here
            System.out.println("[Client] Sent " + out.size() + " bytes for " + levels.length + " readings");

            for (int i = 0; i < levels.length; i++) {
                String status = in.readUTF();
                System.out.printf("[Client] Reading %d (%.2f m): %s%n", i + 1, levels[i], status);
            }
        } catch (IOException e) {
            System.err.println("[Client] Cannot reach " + HOST + ":" + PORT + " - " + e.getMessage());
        }
    }
}