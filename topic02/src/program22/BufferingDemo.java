package program22;

/*
 * Program 2.2: BufferingDemo.java
 *
 * KitaHub's Melaka hub receives a manifest file of 20,000 parcels each
 * day. This program reads the same file one byte at a time, first without
 * a buffer and then with a BufferedInputStream, and counts how many
 * requests reach the file each time.
 */
import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class BufferingDemo {

    /** Counts every request that reaches the stream it wraps. */
    static class CountingInputStream extends FilterInputStream {
        long requests = 0;

        CountingInputStream(InputStream in) { super(in); }

        @Override
        public int read() throws IOException {
            requests++;
            return super.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            requests++;
            return super.read(b, off, len);
        }
    }

    public static void main(String[] args) throws IOException {
        Path manifest = createSampleManifest(20_000);
        System.out.println("Manifest size: " + Files.size(manifest) + " bytes");
        System.out.println();

        // 1. No buffer: every read() goes straight to the file
        long start = System.nanoTime();
        try (CountingInputStream counter =
                     new CountingInputStream(Files.newInputStream(manifest))) {
            while (counter.read() != -1) { /* read one byte */ }
            report("Unbuffered", counter.requests, start);
        }

        // 2. With a buffer: read() is answered from memory
        start = System.nanoTime();
        try (CountingInputStream counter =
                     new CountingInputStream(Files.newInputStream(manifest));
             InputStream in = new BufferedInputStream(counter)) {
            while (in.read() != -1) { /* read one byte */ }
            report("Buffered", counter.requests, start);
        }
    }

    static void report(String label, long requests, long start) {
        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("%-10s : %,10d requests to the file, %,6d ms%n",
                label, requests, ms);
    }

    /** Writes a manifest with one line per parcel. */
    static Path createSampleManifest(int parcels) throws IOException {
        Path file = Path.of("manifest-MLK.csv");
        StringBuilder sb = new StringBuilder("parcelId,origin,destination,weightKg\n");
        String[] hubs = {"KL", "MLK", "JB"};
        for (int i = 1; i <= parcels; i++) {
            sb.append(String.format(Locale.ROOT, "KH%06d,MLK,%s,%.1f%n",
                    i, hubs[i % 3], 0.5 + (i % 40) * 0.5));
        }
        Files.writeString(file, sb.toString());
        return file;
    }
}
