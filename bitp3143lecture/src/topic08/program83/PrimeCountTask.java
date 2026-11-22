package topic08.program83;

import java.util.concurrent.Callable;

import topic08.program82.PrimeUtil;

/**
 * Program 8.3 (part 1): 
 * This class represent a task that returns the number of primes
 * in its slice. Callable lets the task hand back a result directly.
 */
public class PrimeCountTask implements Callable<Long> {

    private final int from;
    private final int to;

    public PrimeCountTask(int from, int to) {
    	
        this.from = from;
        this.to = to;
    }

    @Override
    public Long call() {
    	
        return PrimeUtil.countPrimes(from, to);
    }
}
