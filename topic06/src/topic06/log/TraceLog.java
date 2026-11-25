package topic06.log;

/**
 * A minimal thread-aware logger.
 * Each line shows the time since start, the thread name and the message.
 */
public class TraceLog {
	
    /** The zero point for every timestamp, recorded when the class loads. */
    private static final long START = System.nanoTime();

    /** Prevents instances: the class is used only through log(). */
    private TraceLog() {
    	
    }

    /**
     * Prints one log line for the calling thread.
     * The time is padded to 8 characters and the thread name to 9,
     * so lines from different threads line up in columns.
     *
     * @param message what the thread did or saw
     */
    public static void log(String message) {
    	
        long micros = (System.nanoTime() - START) / 1_000;
        String thread = Thread.currentThread().getName();
        System.out.printf("%8d us  [%-9s] %s%n", micros, thread, message);
    }
}