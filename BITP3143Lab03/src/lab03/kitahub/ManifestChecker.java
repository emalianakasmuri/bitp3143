package lab03.kitahub;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/** Exercise 2B model solution: checks a KitaHub manifest and splits it into valid records and an error log. */
public class ManifestChecker {

    static final Set<String> HUBS = Set.of("KUL", "MLK", "JHR");
    static final double MAX_WEIGHT_KG = 30.0;

    /** Validates one manifest line. Returns a ParcelRecord or throws ManifestRecordException. */
    public static ParcelRecord parseRecord(String line, int lineNo) throws ManifestRecordException {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new ManifestRecordException(lineNo, "expected 4 fields but found " + parts.length);
        }
        String id = parts[0].trim();
        String origin = parts[1].trim();
        String destination = parts[2].trim();

        if (!HUBS.contains(origin)) {
            throw new ManifestRecordException(lineNo, "unknown origin hub " + origin);
        }
        if (!HUBS.contains(destination)) {
            throw new ManifestRecordException(lineNo, "unknown destination hub " + destination);
        }
        double weight;
        try {
            weight = Double.parseDouble(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new ManifestRecordException(lineNo, "weight is not a number: " + parts[3].trim());
        }
        if (weight <= 0 || weight > MAX_WEIGHT_KG) {
            throw new ManifestRecordException(lineNo, "weight must be above 0 and at most 30 kg, not " + weight);
        }
        return new ParcelRecord(id, origin, destination, weight);
    }

    public static void main(String[] args) {
        String inFile = (args.length > 0) ? args[0] : "manifest_MLK_001.txt";
        String validFile = inFile.replace(".txt", "_valid.txt");
        String errorFile = "manifest_errors.log";
        int lineNo = 0, valid = 0, rejected = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inFile));
             PrintWriter validOut = new PrintWriter(new FileWriter(validFile));
             PrintWriter errorOut = new PrintWriter(new FileWriter(errorFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank() || line.startsWith("#")) continue;
                try {
                    ParcelRecord parcel = parseRecord(line, lineNo);
                    validOut.println(parcel.toLine());
                    valid++;
                } catch (ManifestRecordException e) {
                    errorOut.println("Line " + e.getLineNo() + ": " + e.getMessage() + " -> " + line);
                    rejected++;
                }
            }
            System.out.println("Checked " + inFile + ": " + (valid + rejected) + " records, "
                    + valid + " valid, " + rejected + " rejected.");
            System.out.println("Valid records: " + validFile + "   Errors: " + errorFile);
        } catch (FileNotFoundException e) {
            System.out.println("Manifest not found: " + inFile + ". Check the file name and try again.");
        } catch (IOException e) {
            System.out.println("I/O problem while checking the manifest: " + e.getMessage());
        }
    }
}
