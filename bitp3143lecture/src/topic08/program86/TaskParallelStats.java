package topic08.program86;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Program 8.6 (part 4): task parallelism.
 * Three different jobs read the same array at the same time.
 * None of them writes to the array, so they can safely share it.
 */
public class TaskParallelStats {

	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
    public static void main(String[] args)
            throws InterruptedException, ExecutionException {
    	
    	// Data values initialize at random
        int[] data = new Random(42).ints(5_000_000, 1, 1_000_000).toArray();
        
        // Thread pool creation
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        // Submitting task to pool
        Future<Long> sum = pool.submit(new SumTask(data));
        Future<Integer> max = pool.submit(new MaxTask(data));
        Future<Long> primes = pool.submit(new PrimeValueCountTask(data));
        
        // Display task results
        System.out.println("Values:  " + data.length);
        System.out.println("Sum:     " + sum.get());
        System.out.println("Maximum: " + max.get());
        System.out.println("Primes:  " + primes.get());
        
        // Shutdown pool
        pool.shutdown();
    }
}