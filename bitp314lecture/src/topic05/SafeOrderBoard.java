package topic05;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Kopi Corner kitchen again, now with the locked 
 * board. Six tickets go on the board and three cook threads take them.
 */
public class SafeOrderBoard {
	
	
	/**
	 * Main entry point to the application.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		// one board, shared by all cooks
		LockedOrderBoard board = new LockedOrderBoard();
		String[] items = {"Kopi O", "Mee goreng", "Roti bakar", "Teh tarik", 
				"Nasi lemak", "Milo ais"};
		
		// Adding order item to the board
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
					// cooking happens outside the lock
					DoubleTakeDemo.pause(20);
				}
			}, "Cook-" + cookIndex);
			
			cooks.add(cook);
			cook.start();
		}
		
		for (Thread cook : cooks) {
			cook.join();
		}
		
		System.out.println("All 6 orders cooked once.");
	}
}