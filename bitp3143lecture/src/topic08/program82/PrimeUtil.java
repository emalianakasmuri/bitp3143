package topic08.program82;

/**
 * Program 8.2a
 * This is a helper class that contains methods for prime numbers.
 * 
 * @author Emaliana Kasmuri, FTMK, UTeM
 */
public class PrimeUtil {

    /** 
     * This method determines if n is a prime number
     * 
     * Returns true if n is a prime number. 
     */
    public static boolean isPrime(int n) {
    	
        if (n < 2) {
        	
            return false;
        }
        
        if (n % 2 == 0) {
        	
            return n == 2;
        }
        
        for (int d = 3; (long) d * d <= n; d += 2) {
        	
            if (n % d == 0) {
            	
                return false;
            }
        }
        return true;
    }

    /** 
     * This method counts the primes in the range [from, to], 
     * both ends included. 
     */
    public static long countPrimes(int from, int to) {
    	
        long count = 0;
        for (int n = from; n <= to; n++) {
        	
            if (isPrime(n)) {
            	
                count++;
            }
        }
        return count;
    }
}
