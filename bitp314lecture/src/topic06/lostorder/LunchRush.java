package topic06.lostorder;

/**
 * Program 6.1, part 2
 * 
 * This class lets two counter tablets post orders to the kitchen board at 
 * the lunch rush.
 */
public class LunchRush {

	// Starts two tablets, waits for both, then prints how many orders reached 
	// the board
	public static void main(String[] args) throws InterruptedException {
		
		OrderBoard board = new OrderBoard();

		Thread tablet1 = new Thread(() -> {
			
			// orders #1 to #20
			for (int n = 1; n <= 20; n++) board.post(n);
		}, "Tablet-1");
		
		Thread tablet2 = new Thread(() -> {
			
			// orders #21 to #40
			for (int n = 21; n <= 40; n++) board.post(n);
		}, "Tablet-2");

		tablet1.start();
		tablet2.start();
		tablet1.join();
		tablet2.join();

		System.out.println("Orders sent:     40");
		System.out.println("Orders on board: " + board.count());
	}
}