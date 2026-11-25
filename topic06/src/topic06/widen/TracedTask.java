package topic06.widen;


/**
 * A task that increments a TracedCounter three times.
 */
public class TracedTask implements Runnable {
	
    /** The counter shared with the other worker thread. */
    private final TracedCounter counter;

    /**
     * Creates a task for one worker thread.
     *
     * @param counter the shared traced counter
     */
    public TracedTask(TracedCounter counter) {
    	
        this.counter = counter;
    }

    /** Increments the shared counter three times. */
    @Override
    public void run() {
    	
        for (int i = 0; i < 3; i++) {
        	
            counter.increment();
        }
    }
}