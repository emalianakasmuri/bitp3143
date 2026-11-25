package program24;

/*
 * Program 2.4: ManifestSummary.java
 *
 * Streams KitaHub's Melaka manifest (20,000 parcels) one line at a time
 * and counts how many parcels, and how many kilograms, go to each hub.
 * Each manifest line has four fields: parcelId,origin,destination,weightKg
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ManifestSummary {

    public static void main(String[] args) throws IOException {
        Path manifest = Path.of("data", "manifest-MLK.csv");
        createSampleManifest(manifest, 20_000);   // setup for this example only

        Map<String, Integer> parcels = new TreeMap<>();
        Map<String, Double> weight = new TreeMap<>();

        // Only one line is held in memory at any moment
        try (BufferedReader in =
                     Files.newBufferedReader(manifest, StandardCharsets.UTF_8)) {
            in.readLine();                               // skip the header line
            String line;
            while ((line = in.readLine()) != null) {
                String[] f = line.split(",");
                String destination = f[2];
                double kg = Double.parseDouble(f[3]);
                parcels.merge(destination, 1, Integer::sum);
                weight.merge(destination, kg, Double::sum);
            }
        }

        System.out.println("Destination | Parcels | Weight (kg)");
        for (String hub : parcels.keySet()) {
            System.out.printf(Locale.ROOT, "%-11s | %7d | %11.1f%n",
                    hub, parcels.get(hub), weight.get(hub));
        }
    }

    static void createSampleManifest(Path file, int count) throws IOException {
        Files.createDirectories(file.getParent());
        String[] hubs = {"KL", "JB", "MLK"};
        StringBuilder sb = new StringBuilder("parcelId,origin,destination,weightKg\n");
        for (int i = 1; i <= count; i++) {
            sb.append(String.format(Locale.ROOT, "KH%06d,MLK,%s,%.1f%n",
                    i, hubs[i % 3], 0.5 + (i % 40) * 0.5));
        }
        Files.writeString(file, sb.toString());
    }
}