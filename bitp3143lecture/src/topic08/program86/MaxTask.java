package topic08.program86;

import java.util.concurrent.Callable;

/** 
 * Program 8.6 (part 2): 
 * This class represent a task that find and return the largest value from 
 * a list of data.
 * 
 * @author Emaliana Kasmuri, FTMK UTeM
 * for BITP 3143 Distributed and Parallel Application Development
 * 
 */

public class MaxTask implements Callable<Integer> {

	// Data structure that represent a list of data to be processes
    private final int[] data;
    
    /**
     * Constructor
     * Initialize data for the structure
     * 
     * @param data
     */
    public MaxTask(int[] data) {
    	
        this.data = data;
    }

  
    /**
     * This method represent a task that return the largest value from the data
     */
    @Override
    public Integer call() {
    	
        int max = Integer.MIN_VALUE;
        
        for (int value : data) {
        	
            if (value > max) {
                max = value;
            }
        }
        
        return max;
    }
}
