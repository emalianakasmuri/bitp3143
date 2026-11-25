package topic06.racecondition;


/**
 * A request counter that is NOT thread-safe.
 * It reproduces the lost-update bug from the opening problem.
 */
public class RequestCounter implements Counter {
	
    /** The running total, shared by every thread that uses this object. */
    private int count = 0;

    /**
     * Adds one to the total.
     * count++ is three steps (read, add one, write back), so two threads
     * can interleave here and one of the updates is lost.
     */
    @Override
    public void increment() {
    	
        count++;
    }

    /**
     * Returns the current total.
     * Without synchronization, the value may be stale.
     */
    @Override
    public int getCount() {
    	
        return count;
    }
}
