package topic06.racecondition;


/**
 * A task that increments a shared counter a fixed number of times.
 * Several threads run their own CountingTask against the same counter.
 */
public class CountingTask implements Runnable {
	
    /** The counter shared with the other worker threads. */
    private final Counter counter;
    
    /** How many times this task calls increment(). */
    private final int times;

    /**
     * Creates a task for one worker thread.
     *
     * @param counter the shared counter to increment
     * @param times   the number of increments to perform
     */
    public CountingTask(Counter counter, int times) {
    	
        this.counter = counter;
        this.times = times;
    }

    /** Runs on the worker thread after start(): increments the counter in a loop. */
    @Override
    public void run() {
    	
        for (int i = 0; i < times; i++) {
            counter.increment();
        }
    }
}