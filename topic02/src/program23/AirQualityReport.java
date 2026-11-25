package program23;

/*
 * Program 2.3 (file 2 of 2): AirQualityReport.java
 *
 * Program description:
 *   Creates a small CSV file of hourly API readings, reads it back line by
 *   line, finds the highest reading for each station and writes a summary
 *   report to a second text file.
 *
 * What it shows:
 *   The java.nio.file API: Path, Files.createDirectories, Files.write,
 *   Files.newBufferedReader and Files.newBufferedWriter, all with an explicit
 *   UTF-8 character set and try-with-resources.
 *
 * Class description:
 *   AirQualityReport holds main plus one method per step: create the input
 *   file, load readings, and write the report.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AirQualityReport {

    /**
     * Entry point: create input, load it, summarise it, write the report.
     *
     * @param args not used
     * @throws IOException if any file operation fails
     */
    public static void main(String[] args) throws IOException {
        Path dataDir = Path.of("data");
        Path input  = dataDir.resolve("api-readings.csv");
        Path report = dataDir.resolve("api-report.txt");

        Files.createDirectories(dataDir);            // no error if it already exists
        createSampleCsv(input);

        Map<String, ApiReading> worst = loadWorstPerStation(input);
        writeReport(report, worst);

        // Echo the report so we can see it in the Console
        System.out.println("Report written to " + report.getFileName());
        for (String line : Files.readAllLines(report, StandardCharsets.UTF_8)) {
            System.out.println(line);
        }
    }

    /**
     * Writes an illustrative CSV file with a header row and 12 readings.
     *
     * @param file where to write
     * @throws IOException if the file cannot be written
     */
    private static void createSampleCsv(Path file) throws IOException {
        List<String> lines = List.of(
            "station,state,hour,api",
            "Bukit Rambai,Melaka,08:00,48",
            "Bukit Rambai,Melaka,09:00,55",
            "Bukit Rambai,Melaka,10:00,61",
            "Batu Muda,Kuala Lumpur,08:00,72",
            "Batu Muda,Kuala Lumpur,09:00,85",
            "Batu Muda,Kuala Lumpur,10:00,79",
            "Klang,Selangor,08:00,95",
            "Klang,Selangor,09:00,108",
            "Klang,Selangor,10:00,114",
            "Pasir Gudang,Johor,08:00,41",
            "Pasir Gudang,Johor,09:00,44",
            "Pasir Gudang,Johor,10:00,39");
        Files.write(file, lines, StandardCharsets.UTF_8);   // creates or overwrites
    }

    /**
     * Reads the CSV one line at a time and keeps the highest reading per station.
     *
     * @param file the CSV file to read
     * @return station name mapped to its worst reading, in file order
     * @throws IOException if the file cannot be read
     */
    private static Map<String, ApiReading> loadWorstPerStation(Path file) throws IOException {
        Map<String, ApiReading> worst = new LinkedHashMap<>();   // keeps insertion order
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            in.readLine();                                        // skip the header row
            String line;
            while ((line = in.readLine()) != null) {             // null means end of file
                ApiReading r = ApiReading.fromCsv(line);
                worst.merge(r.station(), r,
                        (oldR, newR) -> newR.api() > oldR.api() ? newR : oldR);
            }
        }
        return worst;
    }

    /**
     * Writes a fixed-width text report of the worst reading per station.
     *
     * @param file  where to write the report
     * @param worst the worst reading for each station
     * @throws IOException if the file cannot be written
     */
    private static void writeReport(Path file, Map<String, ApiReading> worst) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            out.write(String.format("%-13s %-13s %5s %4s  %s%n",
                    "Station", "State", "Hour", "API", "Status"));
            for (ApiReading r : worst.values()) {
                out.write(String.format("%-13s %-13s %5s %4d  %s%n",
                        r.station(), r.state(), r.hour(), r.api(),
                        ApiReading.category(r.api())));
            }
        }   // closing the writer flushes the buffer to disk
    }
}

