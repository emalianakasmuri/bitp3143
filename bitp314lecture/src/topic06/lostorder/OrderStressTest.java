package topic06.lostorder;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// This class reproduces the disappearing order on demand ()
/**
 * Program 6.2
 * 
 * This class reproduces the disappearing order on demand
 */
public class OrderStressTest {

	static final int TABLETS = 2;
	
	// a whole term of lunch rushes, squeezed into one run
	static final int ORDERS_EACH = 50_000;
	
	static final int TRIALS = 20;

	// 
	/**
	 * This method runs every trial and reports how many of them lost orders
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		int failedTrials = 0;
		for (int trial = 1; trial <= TRIALS; trial++) {
			
			int onBoard = runTrial();
			int expected = TABLETS * ORDERS_EACH;
			if (onBoard != expected) {
				
				failedTrials++;
				
				System.out.printf("Trial %2d: %d orders missing%n", trial, 
					expected - onBoard);
			}
		}
		
		System.out.printf("Failed %d of %d trials%n", failedTrials, TRIALS);
	}

	/**
	 * This method runs one lunch rush with all tablets released together, and 
	 * returns the number of orders on the board
	 *
	 * @return board size
	 * @throws InterruptedException
	 */
	public static int runTrial() throws InterruptedException {
		
		// the unsafe kitchen board
		List<Integer> board = new ArrayList<>();
		
		// holds every tablet at the line
		CountDownLatch startGate = new CountDownLatch(1);
		Thread[] tablets = new Thread[TABLETS];

		for (int tabletNo = 0; tabletNo < TABLETS; tabletNo++) {
			
			int first = tabletNo * ORDERS_EACH + 1;
			tablets[tabletNo] = new Thread(() -> {
				
				try {
					
					// wait for the starting signal
					startGate.await();
					
					for (int no = first; no < first + ORDERS_EACH; no++) {
						board.add(no);
					}
					
				} catch (InterruptedException e) {
					
					Thread.currentThread().interrupt();
					
				} catch (RuntimeException e) {
					
					// a crash also loses orders
					System.out.println(Thread.currentThread().getName() 
						+ " crashed: " + e);
				}
			}, "Tablet-" + (tabletNo + 1));
			tablets[tabletNo].start();
		}

		// all tablets start together
		startGate.countDown();
		for (Thread tablet : tablets) {
			tablet.join();
		}
		
		return board.size();
	}
}