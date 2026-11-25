package topic06.test;


import java.util.concurrent.CountDownLatch;

import topic06.racecondition.Counter;

/**
 * A counting task that waits at a start gate before it begins.
 * The gate lets all threads start counting at the same moment.
 */
public class GatedCountingTask implements Runnable {
	
    /** The counter shared with the other worker threads. */
    private final Counter counter;
    
    /** How many times this task calls increment(). */
    private final int times;
    
    /** The latch every worker waits on until the test releases it. */
    private final CountDownLatch startGate;

    /**
     * Creates a gated task for one worker thread.
     *
     * @param counter   the shared counter to increment
     * @param times     the number of increments to perform
     * @param startGate the latch to wait on before counting
     */
    public GatedCountingTask(Counter counter, int times, CountDownLatch startGate) {
    	
        this.counter = counter;
        this.times = times;
        this.startGate = startGate;
    }

    /** 
     * Waits for the gate to open, then increments the counter. 
     */
    @Override
    public void run() {
    	
        try {
        	
            // Block until every thread is ready and the gate is released.
            startGate.await();
            
        } catch (InterruptedException e) {
        	
            Thread.currentThread().interrupt();
            return;
        }
        
        for (int i = 0; i < times; i++) {
        	
            counter.increment();
        }
    }
}