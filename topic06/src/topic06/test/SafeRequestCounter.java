package topic06.test;

import topic06.racecondition.Counter;

/**
 * A thread-safe request counter.
 * Both methods are synchronized, so only one thread at a time can use them.
 */
public class SafeRequestCounter implements Counter {
	
    /** The running total, guarded by this object's lock. */
    private int count = 0;

    /** Adds one to the total; the read, add and write cannot be split. */
    @Override
    public synchronized void increment() {
    	
        count++;
    }

    /** Returns the latest total, read under the same lock. */
    @Override
    public synchronized int getCount() {
    	
        return count;
    }
}