package topic05;

/**
 * This class represents a Kopi Corner morning. Kak Ros restocks the egg tray
 * while Cook-2 needs 2 eggs for nasi lemak.
 */
public class EggTrayLock {

	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		// one tray, shared by Kak Ros and Cook-2
		EggTray tray = new EggTray();

		Thread rosThread = new Thread(() -> tray.restock(30), "Kak-Ros");
		
		Thread cook = new Thread(() -> {
			
			try {
				
				// arrives while Kak Ros is counting
				pause(50);
				if (tray.tryUseEggs(2)) {
					
					System.out.println("Cook-2 used 2 eggs for nasi lemak.");
					
				} else {
					
					System.out.println("Cook-2 could not reach the tray in time. "
							+ "Makes roti bakar first.");
				}
				
				// tries again later
				pause(400);
				if (tray.tryUseEggs(2)) {
					
					System.out.println("Cook-2 used 2 eggs for nasi lemak.");
					
				}
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
			}
		}, "Cook-2");

		rosThread.start();
		cook.start();
		
		rosThread.join();
		cook.join();
	}

	/**
	 * This method Makes the current thread wait for ms milliseconds (simulates work)
	 * @param ms
	 */
	static void pause(long ms) {
		
		try {
			
			Thread.sleep(ms);
			
		} catch (InterruptedException e) {
			
			Thread.currentThread().interrupt();
		}
	}

}