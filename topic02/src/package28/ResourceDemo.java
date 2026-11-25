package package28;

/*
 * Program 2.8: ResourceDemo.java
 *
 * Part A: reads Kopi Corner's menu file twice, first with a finally block
 *         (the style used before Java 7), then with try-with-resources.
 * Part B: two resources opened in one try-with-resources statement, to
 *         show the closing order and a suppressed exception.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResourceDemo {

    public static void main(String[] args) throws IOException {
        Path menu = Path.of("menu.csv");
        Files.write(menu, List.of("code,name,price", "D01,Kopi O,2.50", "D02,Teh Tarik,2.80"));

        System.out.println("=== Part A ===");
        System.out.println("Old style: " + countItemsOldStyle(menu) + " items");
        System.out.println("New style: " + countItemsNewStyle(menu) + " items");

        System.out.println();
        System.out.println("=== Part B ===");
        try (Device tablet = new Device("Counter tablet", false);
             Device screen = new Device("Kitchen screen", true)) {
            System.out.println("Sending order to kitchen...");
            throw new IOException("order could not be sent");
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("Suppressed: " + s.getMessage());
            }
        }
    }

    // Before Java 7: close the reader yourself in finally
    static int countItemsOldStyle(Path menu) throws IOException {
        BufferedReader in = null;
        try {
            in = Files.newBufferedReader(menu);
            in.readLine();                       // skip header
            int count = 0;
            while (in.readLine() != null) count++;
            return count;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("close failed: " + e.getMessage());
                }
            }
        }
    }

    // Java 7 and later: the reader is closed for you
    static int countItemsNewStyle(Path menu) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(menu)) {
            in.readLine();                       // skip header
            int count = 0;
            while (in.readLine() != null) count++;
            return count;
        }
    }

    /** A pretend device that reports when it is closed. */
    static class Device implements AutoCloseable {
        private final String name;
        private final boolean failOnClose;

        Device(String name, boolean failOnClose) {
            this.name = name;
            this.failOnClose = failOnClose;
            System.out.println("Open  " + name);
        }

        @Override
        public void close() throws IOException {
            System.out.println("Close " + name);
            if (failOnClose) {
                throw new IOException(name + " did not close cleanly");
            }
        }
    }
}