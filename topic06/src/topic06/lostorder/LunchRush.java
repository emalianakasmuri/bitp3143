package topic06.lostorder;


/**
 * (Program 6.1, part 2)
 * 
 * This class lets two counter tablets post orders to the kitchen board at the lunch rush 
 */
public class LunchRush {

	/**
	 * Main entry point to the application.
	 * 
	 * The program starts two tablets, waits for both, then prints how many orders 
	 * reached the board
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		// Create order board object
		OrderBoard board = new OrderBoard();
		
		// Create to tablets in separate threads, named Tablet-1 and Tablet-2
		// with both create 20 order and post it to the board
		Thread tablet1 = new Thread(() -> {
			
			// Add orders between #1 to #20
			for (int orderNo = 1; orderNo <= 20; orderNo++) 
				board.post(orderNo);
			
		}, "Tablet-1");
		
		Thread tablet2 = new Thread(() -> {
			
			// orders #21 to #40
			for (int orderNo = 21; orderNo <= 40; orderNo++) 
				board.post(orderNo);
			
		}, "Tablet-2");
		
		// Start threads
		tablet1.start();
		tablet2.start();
		
		// Join the main thread
		tablet1.join();
		tablet2.join();

		System.out.println("Orders sent:     40");
		System.out.println("Orders on board: " + board.count());
	}
}