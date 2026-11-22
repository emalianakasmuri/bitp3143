package topic08.program84;

import java.util.stream.IntStream;

import topic08.program82.PrimeUtil;

/**
 * Program 8.4: 
 * This class counts the primes from 1 to LIMIT twice using Java streams:
 *   1. with a sequential stream, on one thread;
 *   2. with a parallel stream, split across all cores.
 * Both runs check exactly the same numbers with the same isPrime() test,
 * so they must give the same count. Only the time should differ, and
 * comparing the two times gives the speedup.
 */
public class StreamPrimeCount {

    // Count the primes from 1 to LIMIT, the same job as Programs 8.2 and 8.3
    public static final int LIMIT = 20_000_000;

    /**
     * Main entry point to the application.
     * @param args
     */
    public static void main(String[] args) {
    	
        long start = System.nanoTime();
        // 1. Sequential stream: one thread checks every number (the baseline, T(1))
        // the numbers 1 to LIMIT
        long sequential = IntStream.rangeClosed(1, LIMIT)
        		// keep only the primes
                .filter(PrimeUtil::isPrime)   
                // how many are left
                .count();                                        
        long seqMs = (System.nanoTime() - start) / 1_000_000;

        start = System.nanoTime();
        // 2. Parallel stream: the same chain with one extra call
        long parallel = IntStream.rangeClosed(1, LIMIT)
        		// the only change: split the range across all cores
                .parallel()   
                .filter(PrimeUtil::isPrime)
                .count();
        long parMs = (System.nanoTime() - start) / 1_000_000;

        // Both counts must match: correctness first, then speed
        System.out.println("Sequential stream: " + sequential + " primes in " 
        	+ seqMs + " ms");
        System.out.println("Parallel stream:   " + parallel + " primes in " 
        	+ parMs + " ms");
        
        // Speedup S(N) = T(1) / T(N), from Section 8.4
        System.out.printf("Speedup: %.2f%n", (double) seqMs / parMs);
    }
}