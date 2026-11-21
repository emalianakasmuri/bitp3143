package topic08.program82;

/**
 * Program 8.2 (part 3): 
 * This class splits the range into one slice per core,
 * start one thread per slice, then join and combine the results.
 */
public class ThreadPrimeCount {

    public static final int LIMIT = 20_000_000;

    /**
     * Main entry point of the application
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
    	
        int cores = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[cores];
        PrimeRangeWorker[] workers = new PrimeRangeWorker[cores];
        int sliceSize = LIMIT / cores;

        long start = System.nanoTime();

        // 1. Split: give each thread its own slice of the range
        for (int i = 0; i < cores; i++) {
        	
            int from = i * sliceSize + 1;
            int to = (i == cores - 1) ? LIMIT : (i + 1) * sliceSize;
            workers[i] = new PrimeRangeWorker(from, to);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        // 2. Wait for every thread to finish
        for (Thread t : threads) {
        	
            t.join();
        }

        // 3. Combine the partial results
        long total = 0;
        for (PrimeRangeWorker w : workers) {
        	
            total += w.getResult();
        }

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Threads used: " + cores);
        System.out.println("Primes up to " + LIMIT + ": " + total);
        System.out.println("Parallel time: " + elapsedMs + " ms");
    }
}
