package topic08.program86;

import java.util.concurrent.Callable;

/** 
 * Program 8.6 (part 1):
 * This class represent a task that adds up every value in the array
 * and expected to return the result from the addition
 * 
 * @author Emaliana Kasmuri, FTMK UTeM
 * for BITP 3143 Distributed and Parallel Application Development
 * 
 */
public class SumTask implements Callable<Long> {

	// Data structure that represent a list of data to be processes
    private final int[] data;

    /**
     * Constructor
     * Initialize data for the structure
     * 
     * @param data
     */
    public SumTask(int[] data) {
    	
        this.data = data;
    }
    
    /**
     * This method represent a task to compute summation from a list of data
     */
    @Override
    public Long call() {
    	
        long sum = 0;
        for (int value : data) {
        	
            sum += value;
        }
        
        return sum;
    }
}