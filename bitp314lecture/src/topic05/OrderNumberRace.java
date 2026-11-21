package topic05;

/**
 * This class represents the Kopi Corner counter at lunch time, without any 
 * protection.  Two tablet threads take 10,000 orders each, sharing one counter.
 */
public class OrderNumberRace {

	public static void main(String[] args) throws InterruptedException {
		
		// one counter, shared by both tablets
		OrderNumberCounter counter = new OrderNumberCounter();

		// What each tablet does: 
		// take 10,000 orders, one receipt number per order
		Runnable tablet = () -> {
			for (int i = 0; i < 10_000; i++) {
				counter.issue();
			}
		};

		Thread tablet1 = new Thread(tablet, "Tablet-1");
		Thread tablet2 = new Thread(tablet, "Tablet-2");
		tablet1.start();
		tablet2.start();
		
		// wait for both tablets to finish
		tablet1.join();
		tablet2.join();

		System.out.println("Orders taken:        20000");
		System.out.println("Last number issued:  " + counter.current());
	}
}
