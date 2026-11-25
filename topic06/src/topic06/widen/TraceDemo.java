package topic06.widen;

import topic06.log.TraceLog;

/**
 * Runs two traced workers so the log shows the exact interleaving.
 */
public class TraceDemo {
	
    /**
     * Starts two workers on one shared counter and logs the final count.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted while waiting
     */
    public static void main(String[] args) throws InterruptedException {
    	
        TracedCounter counter = new TracedCounter();
        Thread t1 = new Thread(new TracedTask(counter), "worker-1");
        Thread t2 = new Thread(new TracedTask(counter), "worker-2");

        t1.start();
        t2.start();
        
        // Wait for both workers before reading the result.
        t1.join();
        t2.join();

        TraceLog.log("final count " + counter.getCount() + " (expected 6)");
    }
}