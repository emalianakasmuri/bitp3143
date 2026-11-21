package topic05;

/**
 * This class represents the single order-number counter at Kopi Corner.
 * Both counter tablets share ONE object of this class, so lastNumber is
 * shared data. Every customer receipt number comes from issue().
 */
public class SafeOrderNumberCounter {

	// last receipt number given out
	private int lastNumber = 0;

	/**
	 * This method gives a tablet the next receipt number.  It is a synchronized
	 * method that a tablet must hold this counter's lock to enter, so only one 
	 * tablet at a time can read, add and write.
	 * 
	 * @return
	 */
	public synchronized int issue() {
		
		// 1. read the last number, add one
		int next = lastNumber + 1;
		
		// let another thread run here
		Thread.yield();
		
		// 2. write it back
		lastNumber = next;
		
		return next;
	}

	/**
	 * This is a synchronized method that returns the last receipt number 
	 * issued.  It always sees the latest value.
	 * @return
	 */
	public synchronized int current() {
		
		return lastNumber;
	}
}