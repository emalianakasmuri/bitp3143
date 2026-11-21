package topic05;

/**
 * This class demonstrate A volatile flag tells the cook thread the kitchen 
 * has closed.
 */
public class KitchenClosing {

	private static volatile boolean open = true;

	public static void main(String[] args) throws InterruptedException {
		
		Thread cook = new Thread(() -> {
			long checks = 0;
			// reads the latest value every time
			while (open) {
				checks++;
			}
			System.out.println("Cook saw the kitchen close after " + checks 
					+ " checks.");
		}, "Cook-1");

		cook.start();
		
		// the kitchen runs for one second
		Thread.sleep(1000);
		
		// Kak Ros closes the kitchen
		open = false;
		System.out.println("Kak Ros: kitchen closed.");
		
		cook.join();
		System.out.println("Cook has gone home.");
	}
}