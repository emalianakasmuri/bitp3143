package lab05.kopicorner.solution;

/*
 * CafeLog.java (Lab 05, provided complete)
 *
 * Program description:
 *   Small helpers shared by the Lab 05 programs. say() prints a message
 *   tagged with the time since the program started and the name of the
 *   thread that printed it. pause() sleeps without forcing every caller to
 *   handle InterruptedException.
 */
public final class CafeLog {

    /** Time at which the program started. */
    private static final long START = System.nanoTime();

    /** No objects needed: every method is static. */
    private CafeLog() {
    }

    /**
     * Prints a message tagged with the elapsed time and the current thread's name.
     *
     * @param message the text to print
     */
    public static void say(String message) {
        System.out.printf("[%4.1fs] %-10s %s%n", seconds(), "[" + Thread.currentThread().getName() + "]", message);
    }

    /**
     * Seconds since the program started.
     *
     * @return elapsed time in seconds
     */
    public static double seconds() {
        return (System.nanoTime() - START) / 1e9;
    }

    /**
     * Sleeps for a while. If the thread is interrupted, the interrupt flag is
     * set again so that the caller can still see it.
     *
     * @param millis how long to sleep, in milliseconds
     */
    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
