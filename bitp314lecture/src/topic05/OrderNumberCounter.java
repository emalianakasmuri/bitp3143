package topic05;

/**
 * This class represent a single order-number counter at Kopi Corner.  
 * It demonstrate an unsafe there where if two tablets call issue() at the 
 * same time, their steps can interleave and both get the same number. 
 * Both counter tablets share ONE object of this class, so lastNumber is shared 
 * data. Every customerreceipt number comes from issue().
 */

public class OrderNumberCounter {

	// last receipt number given out
	private int lastNumber = 0;

	
	/**
	 * This method gives a tablet the next receipt number. 
	 * It is NOT a thread-safe because another tablet can run between the read 
	 * and the write.
	 *
	 * @return
	 */
	// 
	public int issue() {
		
		// 1. read the last number, add one
		int next = lastNumber + 1;
		
		// let another thread run here
		Thread.yield();
		
		// 2. write it back
		
		lastNumber = next;
		return next;
	}

	/**
	 * Returns the last receipt number issued
	 * @return
	 */
	public int current() {
		
		return lastNumber;
	}
}
