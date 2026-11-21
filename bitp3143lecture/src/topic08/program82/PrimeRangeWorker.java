package topic08.program82;

/**
 * Program 8.2 (part 2)
 * This calss counts the primes in one slice of the range.
 * Each worker runs on its own thread and keeps its own result,
 * so no two threads ever write to the same variable.
 * 
 * @author Emaliana Kasmuri, FTMK, UTeM
 */
public class PrimeRangeWorker implements Runnable {

    private final int from;
    private final int to;
    private long result;

    public PrimeRangeWorker(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        result = PrimeUtil.countPrimes(from, to);
    }

    public long getResult() {
        return result;
    }
}