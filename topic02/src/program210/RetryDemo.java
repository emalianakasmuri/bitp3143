package program210;

/*
 * Program 2.10: RetryDemo.java
 *
 * At the end of the day, the Melaka hub server copies its manifest to
 * KitaHub's shared storage. While the nightly backup is running, the
 * storage is sometimes busy for a moment. The program retries that
 * transient error with exponential backoff, but gives up at once on a
 * permanent error such as a refused permission.
 *
 * The shared storage is simulated, so the program runs on any computer.
 */
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class RetryDemo {

    /** A temporary problem: trying again later may work. */
    static class StorageBusyException extends IOException {
        StorageBusyException(String message) { super(message); }
    }

    /** Pretend storage that is busy for the first few attempts. */
    static class SharedStorage {
        private int busyAttemptsLeft;
        private final boolean permissionRefused;

        SharedStorage(int busyAttempts, boolean permissionRefused) {
            this.busyAttemptsLeft = busyAttempts;
            this.permissionRefused = permissionRefused;
        }

        String upload(String fileName) throws IOException {
            if (permissionRefused) {
                throw new AccessDeniedException(fileName, null, "hub account has no write permission");
            }
            if (busyAttemptsLeft > 0) {
                busyAttemptsLeft--;
                throw new StorageBusyException("storage busy, backup running");
            }
            return "STORED-" + fileName;
        }
    }

    static final int MAX_ATTEMPTS = 4;
    static final long FIRST_DELAY_MS = 100;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Night 1: storage busy twice ===");
        run(new SharedStorage(2, false));

        System.out.println();
        System.out.println("=== Night 2: storage busy all night ===");
        run(new SharedStorage(10, false));

        System.out.println();
        System.out.println("=== Night 3: permission refused ===");
        run(new SharedStorage(0, true));
    }

    static void run(SharedStorage storage) throws InterruptedException {
        try {
            String ref = uploadWithRetry(storage, "manifest-MLK-2026-10-08.csv");
            System.out.println("Done: " + ref);
        } catch (IOException e) {
            System.out.println("FAILED: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("  last cause: " + e.getCause().getMessage());
            }
        }
    }

    static String uploadWithRetry(SharedStorage storage, String fileName)
            throws IOException, InterruptedException {
        long delay = FIRST_DELAY_MS;
        for (int attempt = 1; ; attempt++) {
            try {
                return storage.upload(fileName);
            } catch (StorageBusyException e) {             // transient: may retry
                if (attempt == MAX_ATTEMPTS) {
                    throw new IOException("gave up after " + attempt + " attempts", e);
                }
                System.out.println("Attempt " + attempt + " failed (" + e.getMessage()
                        + "), waiting " + delay + " ms");
                Thread.sleep(delay);                     // pause before the next try
                delay *= 2;                              // 100, 200, 400 ...
            }
            // Any other IOException (such as AccessDeniedException) is not
            // caught here, so it leaves the method at once: no retry.
        }
    }
}