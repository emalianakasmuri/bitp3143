package program29;

/*
 * Program 2.9: ManifestValidator.java
 *
 * Checks every record in a KitaHub manifest file. A bad record is
 * reported and skipped, so one broken line never stops the whole lorry
 * load. Each problem is reported as an InvalidParcelRecordException that
 * names the line number and, where there is one, keeps the original
 * cause (exception chaining).
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ManifestValidator {

    /** Custom checked exception for one bad manifest record. */
    static class InvalidParcelRecordException extends Exception {
        private final int lineNumber;

        InvalidParcelRecordException(int lineNumber, String message) {
            super("line " + lineNumber + ": " + message);
            this.lineNumber = lineNumber;
        }

        InvalidParcelRecordException(int lineNumber, String message, Throwable cause) {
            super("line " + lineNumber + ": " + message, cause);
            this.lineNumber = lineNumber;
        }

        int getLineNumber() { return lineNumber; }
    }

    static final Set<String> HUBS = Set.of("KL", "MLK", "JB");

    public static void main(String[] args) throws IOException {
        Path manifest = Path.of("manifest-lorry-17.csv");
        Files.write(manifest, List.of(                    // setup for this example
                "parcelId,origin,destination,weightKg",
                "KH000201,MLK,JB,2.5",
                "KH000202,MLK,KL",
                "KH000203,MLK,PENANG,1.0",
                "KH000204,MLK,KL,abc",
                "KH000205,MLK,JB,0",
                "KH000206,MLK,KL,3.2"), StandardCharsets.UTF_8);

        int accepted = 0, rejected = 0;
        try (BufferedReader in = Files.newBufferedReader(manifest, StandardCharsets.UTF_8)) {
            in.readLine();                                // header is line 1
            String line;
            int lineNumber = 1;
            while ((line = in.readLine()) != null) {
                lineNumber++;
                try {
                    String id = validate(line, lineNumber);
                    accepted++;
                    System.out.println("OK       " + id);
                } catch (InvalidParcelRecordException e) {
                    rejected++;
                    System.out.println("REJECTED " + e.getMessage());
                    if (e.getCause() != null) {
                        System.out.println("         cause: " + e.getCause());
                    }
                }
            }
        }
        System.out.println("Accepted " + accepted + ", rejected " + rejected);
    }

    /** Returns the parcel ID if the record is valid; throws if it is not. */
    static String validate(String line, int lineNumber) throws InvalidParcelRecordException {
        String[] f = line.split(",");
        if (f.length != 4) {
            throw new InvalidParcelRecordException(lineNumber,
                    "expected 4 fields but found " + f.length);
        }
        if (!HUBS.contains(f[2])) {
            throw new InvalidParcelRecordException(lineNumber,
                    "unknown destination hub '" + f[2] + "'");
        }
        double kg;
        try {
            kg = Double.parseDouble(f[3]);
        } catch (NumberFormatException e) {
            throw new InvalidParcelRecordException(lineNumber,
                    "weight is not a number", e);      // keep the original cause
        }
        if (kg <= 0) {
            throw new InvalidParcelRecordException(lineNumber,
                    "weight must be more than 0 kg");
        }
        return f[0];
    }
}