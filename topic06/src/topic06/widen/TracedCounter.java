package topic06.widen;

import topic06.log.TraceLog;

/**
 * A counter that splits count++ into a logged read and a logged write,
 * so the interleaving of two threads becomes visible.
 */
public class TracedCounter {
	
    /** The running total shared by the worker threads. */
    private int count = 0;

    /**
     * Adds one to the total in two explicit steps.
     * If two threads read the same value, one increment is lost.
     */
    public void increment() {
    	
        // Step 1: read the current value.
        int seen = count;
        TraceLog.log("read  " + seen);
        
        // Give the scheduler a chance to switch threads between read and write.
        Thread.yield();
        
        // Step 2: write the new value back.
        count = seen + 1;
        TraceLog.log("wrote " + (seen + 1));
    }

    /** @return the current total */
    public int getCount() {
    	
        return count;
    }
}