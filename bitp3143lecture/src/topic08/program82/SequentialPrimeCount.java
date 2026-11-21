package topic08.program82;

/**
 * Program 8.2 (part 1): 
 * This class count primes up to LIMIT on one core.
 * 
 * @author Emaliana Kasmuri, FTMK, UTeM
 */
public class SequentialPrimeCount {

    public static final int LIMIT = 20_000_000;

    /**
     * Main entry point to the application
     * @param args
     */
    public static void main(String[] args) {
    	
        long start = System.nanoTime();
        long count = PrimeUtil.countPrimes(1, LIMIT);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("Primes up to " + LIMIT + ": " + count);
        System.out.println("Sequential time: " + elapsedMs + " ms");
    }
}
