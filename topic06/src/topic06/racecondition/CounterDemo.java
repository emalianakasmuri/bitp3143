package topic06.racecondition;

/**
 * Demonstrates the race condition in RequestCounter.
 * Four threads each add 100,000, so every run should print 400,000.
 */
public class CounterDemo {
    /**
     * Repeats the experiment five times and prints the expected and actual totals.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted while waiting
     */
    public static void main(String[] args) throws InterruptedException {
    	
        final int threadCount = 4;
        final int perThread = 100_000;

        for (int run = 1; run <= 5; run++) {
        	
            Counter counter = new RequestCounter();
            Thread[] workers = new Thread[threadCount];

            // Create and start the named worker threads.
            for (int i = 0; i < threadCount; i++) {
                workers[i] = new Thread(new CountingTask(counter, perThread), 
                		"worker-" + (i + 1));
                workers[i].start();
            }
            
            // Wait until every worker has finished before reading the total.
            for (Thread t : workers) {
                t.join();
            }
            
            System.out.printf("Run %d: expected %d, got %d%n",
                    run, threadCount * perThread, counter.getCount());
        }
    }
}