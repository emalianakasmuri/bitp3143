package topic06.ordertrace;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Program 6.3, part 2
 * 
 * This class traces every post to find out where the missing order went.
 */
public class TracedLunchRush {

	static final ConcurrentLinkedQueue<OrderEvent> trace = 
		new ConcurrentLinkedQueue<>();

	

	/**
	 * Main entry point to the application which repeats trials until orders 
	 * go missing, then explains the first missing order
	 *
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		for (int trial = 1; trial <= 50; trial++) {
			
			trace.clear();
			List<Integer> board = runTrial(50_000);
			if (board.size() == 100_000) {
				
				// no loss this time, try again
				continue;
			}
			
			Set<Integer> onBoard = new HashSet<>(board);
			int missing = -1;
			for (int number = 1; number <= 100_000 && missing < 0; number++) {
				
				// first missing order number
				if (!onBoard.contains(number)) missing = number;
			}
			
			System.out.printf("Trial %d: %d orders missing. First missing: #%d%n",
					trial, 100_000 - board.size(), missing);
			explain(missing);
			
			return;
		}
		
		System.out.println("No orders lost in 50 trials.");
	}

	/**
	 * This method prints every event that saw the same board size as 
	 * the missing order
	 * 
	 * @param missing
	 */
	public static void explain(int missing) {
		
		OrderEvent lost = null;
		
		for (OrderEvent event : trace) {
			
			if (event.orderNo() == missing) 
				lost = event;
		}
		
		System.out.println("Events that saw the same board size:");
		for (OrderEvent event : trace) {
			
			if (event.boardSizeSeen() == lost.boardSizeSeen()) {
				
				System.out.printf("  %-8s posted #%-6d  board size seen: %d%n",
						event.thread(), event.orderNo(), event.boardSizeSeen());
			}
		}
	}

	/**
	 * This method runs one traced lunch rush and returns the board
	 * 
	 * @param ordersEach
	 * @return
	 * @throws InterruptedException
	 */
	public static List<Integer> runTrial(int ordersEach) throws InterruptedException {
		
		List<Integer> board = new ArrayList<>();
		CountDownLatch startGate = new CountDownLatch(1);
		Thread[] tablets = new Thread[2];
		
		for (int no = 0; no < 2; no++) {
			
			int first = no * ordersEach + 1;
			tablets[no] = new Thread(() -> {
				
				try {
					
					startGate.await();
					
					for (int n = first; n < first + ordersEach; n++) {
						
						// what this tablet sees
						int seen = board.size();
						board.add(n);
						
						// record it, cheaply
						log(n, seen);
					}
					
				} catch (InterruptedException e) {
					
					Thread.currentThread().interrupt();
				}
			}, "Tablet-" + (no + 1));
			
			tablets[no].start();
		}
		
		startGate.countDown();
		for (Thread tablet : tablets)
			tablet.join();
		
		return board;
	}
	
	/**
	 * This method records one post in the trace: time, thread, order number and 
	 * the board size seen
	 * 
	 * @param orderNo
	 * @param boardSizeSeen
	 */
	public static void log(int orderNo, int boardSizeSeen) {
		
		trace.add(new OrderEvent(System.nanoTime(), 
				Thread.currentThread().getName(), orderNo, boardSizeSeen));
	}
}