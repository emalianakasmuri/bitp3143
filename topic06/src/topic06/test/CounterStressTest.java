package topic06.test;

import java.util.concurrent.CountDownLatch;

import topic06.racecondition.Counter;
import topic06.racecondition.RequestCounter;

/**
 * A stress test that runs many trials and counts how many give a wrong total.
 * Pass the argument "safe" to test SafeRequestCounter instead of RequestCounter.
 */
public class CounterStressTest {
	
    /** The number of trials to run. */
    private static final int TRIALS = 50;
    
    /** The number of worker threads in each trial. */
    private static final int THREADS = 4;
    
    /** The number of increments each worker performs. */
    private static final int PER_THREAD = 100_000;

    /**
     * Runs every trial and prints a one-line summary.
     *
     * @param args "safe" to test the corrected counter; empty for the faulty one
     * @throws InterruptedException if the main thread is interrupted while waiting
     */
    public static void main(String[] args) throws InterruptedException {
    	
        boolean useSafe = args.length > 0 && args[0].equals("safe");
        int failures = 0;
        int worst = THREADS * PER_THREAD;

        for (int trial = 1; trial <= TRIALS; trial++) {
        	
            Counter counter = useSafe ? new SafeRequestCounter() : new RequestCounter();
            int result = runTrial(counter);
            
            // Any total other than THREADS x PER_THREAD is a failure.
            if (result != THREADS * PER_THREAD) {
            	
                failures++;
                worst = Math.min(worst, result);
            }
        }

        System.out.printf("%s: %d of %d trials failed (lowest total %d, expected %d)%n",
                counterName(useSafe), failures, TRIALS, worst, THREADS * PER_THREAD);
    }

    /**
     * Runs one trial: starts every worker, releases them together, and waits.
     *
     * @param counter the counter under test
     * @return the total after all workers finish
     * @throws InterruptedException if the main thread is interrupted while waiting
     */
    private static int runTrial(Counter counter) throws InterruptedException {
    	
        CountDownLatch startGate = new CountDownLatch(1);
        Thread[] workers = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            workers[i] = new Thread(new GatedCountingTask(counter, PER_THREAD, startGate),
            	"worker-" + (i + 1));
            workers[i].start();
        }
        
        // Release all waiting workers at the same moment.
        startGate.countDown();
        
        for (Thread t : workers) {
            t.join();
        }
        
        return counter.getCount();
    }

    /**
     * @param useSafe whether the corrected counter was tested
     * @return the class name to print in the summary
     */
    private static String counterName(boolean useSafe) {
    	
        return useSafe ? "SafeRequestCounter" : "RequestCounter";
    }
}