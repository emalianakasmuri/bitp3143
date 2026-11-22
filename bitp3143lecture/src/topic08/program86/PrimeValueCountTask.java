package topic08.program86;

import java.util.concurrent.Callable;

import topic08.program82.PrimeUtil;

/** 
 * Program 8.6 (part 3): 
 * This class represent a task that counts the number of prime number in the 
 * array.
 * 
 * @author Emaliana Kasmuri, FTMK UTeM
 * for BITP 3143 Distributed and Parallel Application Development
 * 
 */
public class PrimeValueCountTask implements Callable<Long> {

	// Data structure that represent a list of data to be processes
    private final int[] data;

    /**
     * Constructor
     * Initialize data for the structure
     * 
     * @param data
     */
    public PrimeValueCountTask(int[] data) {
        this.data = data;
    }

    /**
     * This method represents a task to count prime numbers in the array
     */
    @Override
    public Long call() {
    	
        long count = 0;
        for (int value : data) {
        	
        	// Validating data as prime number
            if (PrimeUtil.isPrime(value)) {
            	
                count++;
            }
        }
        return count;
    }
}
