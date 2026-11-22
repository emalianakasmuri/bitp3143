package topic08.program83;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Program 8.3 (part 2): 
 * This class run on a fixed thread pool.
 * Tasks are smaller than one-per-core so busy cores can share the load.
 */
public class ExecutorPrimeCount {

    public static final int LIMIT = 20_000_000;
    public static final int TASKS = 64;
    
    /**
     * Main entry point to the application
     * 
     * @param args
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void main(String[] args)
            throws InterruptedException, ExecutionException {
    	
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        List<Future<Long>> futures = new ArrayList<>();
        int sliceSize = LIMIT / TASKS;

        long start = System.nanoTime();
        for (int i = 0; i < TASKS; i++) {
        	
            int from = i * sliceSize + 1;
            int to = (i == TASKS - 1) ? LIMIT : (i + 1) * sliceSize;
            futures.add(pool.submit(new PrimeCountTask(from, to)));
        }

        long total = 0;
        for (Future<Long> f : futures) {
        	
        	 // waits until that task is done
            total += f.get();  
        }
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        pool.shutdown();

        System.out.println("Pool threads: " + cores + ", tasks: " + TASKS);
        System.out.println("Primes up to " + LIMIT + ": " + total);
        System.out.println("Executor time: " + elapsedMs + " ms");
    }
}
