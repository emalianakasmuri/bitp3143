package topic05;


import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the Kopi Corner kitchen. It puts six orders on the 
 * board and starts three cook threads that take orders from it 
 * (check-then-act race).
 */
public class DoubleTakeDemo {

	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		// one order board, shared by all cooks
		OrderBoard board = new OrderBoard();
		String[] items = {"Kopi O", "Mee goreng", "Roti bakar", "Teh tarik", 
				"Nasi lemak", "Milo ais"};
		
		// Add order to the board
		for (int index = 0; index < items.length; index++) {
			
			board.add(new Order(101 + index, items[index]));
		}

		
		List<Thread> cooks = new ArrayList<>();
		// three cooks (threads), all reading the same order board
		for (int cookIndex = 1; cookIndex <= 3; cookIndex++) {
			
			Thread cook = new Thread(() -> {
				Order order;
				while ((order = board.takeNext()) != null) {
					System.out.println(Thread.currentThread().getName()
							+ " cooks #" + order.number + " " + order.item);
				}
			}, "Cook-" + cookIndex);
			
			cooks.add(cook);
			
			cook.start();
		}
		
		for (Thread cook : cooks) {
			cook.join();
		}
		
		System.out.println("Orders on board: 6. Count the dishes cooked above.");
	}
	
	
	/**
	 * This method Makes the current thread wait for ms milliseconds to 
	 * simulates the work
	 * @param ms
	 */
	public static void pause(long ms) {
		
		try {
			
			Thread.sleep(ms);
			
		} catch (InterruptedException e) {
			
			Thread.currentThread().interrupt();
		}
	}	
			
}
			
			
			
