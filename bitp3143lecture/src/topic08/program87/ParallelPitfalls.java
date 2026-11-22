package topic08.program87;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import topic08.program82.PrimeUtil;

/**
 * Program 8.7: 
 * This class demonstrate two pitfalls of parallel streams.
 * Pitfall 1: writing to a shared, non-thread-safe list.
 * Pitfall 2: going parallel on work that is too small.
 * 
 * @author Emaliana Kasmuri, FTMK UTeM
 * for BITP 3143 Distributed and Parallel Application Development
 */
public class ParallelPitfalls {
	
	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
    	
        // Pitfall 1: shared mutable state
        List<Integer> unsafe = new ArrayList<>();
        try {
        	
            IntStream.rangeClosed(1, 1_000_000)
                    .parallel()
                    .filter(PrimeUtil::isPrime)
                    // many threads add at once
                    .forEach(unsafe::add);
            
            System.out.println("Unsafe list size: " + unsafe.size());
            
        } catch (RuntimeException e) {
        	
            System.out.println("Unsafe list failed: " + e);
        }

        // Fix: let the stream build the result for you
        List<Integer> safe = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .filter(PrimeUtil::isPrime)
                .boxed()
                .toList();
        System.out.println("Safe list size:   " + safe.size() 
        	+ " (correct answer: 78498)");

        // Pitfall 2: too little work to share.
        // Each version runs 2,000 times, and only the last 1,000 runs
        // are timed, so start-up costs do not distort the result.
        long seqNanos = timeSmallCount(false);
        long parNanos = timeSmallCount(true);
        System.out.println("Count primes in 1..1,000, average per run:");
        System.out.println("  sequential: " + seqNanos / 1_000 + " microseconds");
        System.out.println("  parallel:   " + parNanos / 1_000 + " microseconds");
    }

    /** 
     * This method returns the average time of one small count, in nanoseconds. 
     */
    private static long timeSmallCount(boolean parallel) {
    	
        long total = 0;
        for (int run = 0; run < 2_000; run++) {
        	
            long start = System.nanoTime();
            IntStream numbers = IntStream.rangeClosed(1, 1_000);
            
            if (parallel) {
            	
                numbers = numbers.parallel();
            }
            
            numbers.filter(PrimeUtil::isPrime).count();
            if (run >= 1_000) {
            	
                total += System.nanoTime() - start;
            }
        }
        
        return total / 1_000;
    }
}