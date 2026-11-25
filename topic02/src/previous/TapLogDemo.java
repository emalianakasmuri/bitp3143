package previous;

/*
 * Program 2.4: TapLogDemo.java
 *
 * Program description:
 *   Stores three rail-transit "tap-out" events in a binary file with
 *   DataOutputStream, reads them back with DataInputStream, and compares the
 *   file size with the same data saved as CSV text.
 *
 * What it shows:
 *   Data streams write Java primitive values (long, int, String via UTF) in a
 *   fixed binary format. Reading must use exactly the same order and types
 *   as writing, because the file itself carries no field names.
 *
 * Class description:
 *   TapLogDemo holds main, a writer method and a reader method.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TapLogDemo {

    // Illustrative data: card number, station code, fare in sen
    private static final long[]   CARDS    = {6_014_000_123_456L, 6_014_000_987_654L, 6_014_000_555_111L};
    private static final String[] STATIONS = {"KJ14", "SBK20", "PY17"};
    private static final int[]    FARES    = {230, 410, 175};

    /**
     * Entry point: write binary and CSV versions, read the binary back.
     *
     * @param args not used
     * @throws IOException if a file operation fails
     */
    public static void main(String[] args) throws IOException {
        Path bin = Path.of("taps.dat");
        Path csv = Path.of("taps.csv");

        writeBinary(bin);
        // Same data as text, one line per tap, "\n" line breaks on every OS
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < CARDS.length; i++) {
            text.append(CARDS[i]).append(',').append(STATIONS[i])
                .append(',').append(FARES[i]).append('\n');
        }
        Files.writeString(csv, text, StandardCharsets.UTF_8);

        readBinary(bin);
        System.out.println("Binary file size: " + Files.size(bin) + " bytes");
        System.out.println("CSV file size   : " + Files.size(csv) + " bytes");
    }

    /**
     * Writes each tap as: long card, UTF station, int fare.
     *
     * @param file destination file
     * @throws IOException if writing fails
     */
    private static void writeBinary(Path file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(file)))) {
            for (int i = 0; i < CARDS.length; i++) {
                out.writeLong(CARDS[i]);       // always 8 bytes
                out.writeUTF(STATIONS[i]);     // 2-byte length + UTF-8 bytes
                out.writeInt(FARES[i]);        // always 4 bytes
            }
        }
    }

    /**
     * Reads taps until the end of the file and prints them.
     *
     * @param file source file
     * @throws IOException if reading fails for a reason other than end of file
     */
    private static void readBinary(Path file) throws IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(file)))) {
            int total = 0;
            while (true) {
                try {
                    long card = in.readLong();        // same order as writing
                    String station = in.readUTF();
                    int fare = in.readInt();
                    total += fare;
                    System.out.printf("Card %d tapped out at %-5s fare RM%.2f%n",
                            card, station, fare / 100.0);
                } catch (EOFException end) {          // normal way to detect the end
                    break;
                }
            }
            System.out.printf("Total fares: RM%.2f%n", total / 100.0);
        }
    }
}
