package topic05;


import java.util.concurrent.CountDownLatch;

/**
 * This class represents the kitchen server which opens for orders only after 
 * three opening jobs finish (CountDownLatch) 
 */
public class KitchenOpening {

	public static void main(String[] args) throws InterruptedException {
		
		String[] jobs = {"Boil water", "Heat griddle", "Load menu file"};
		
		// simulated time per job (ms)
		int[] millis = {500, 300, 200};
		
		CountDownLatch openingDone = new CountDownLatch(jobs.length);
		long start = System.currentTimeMillis();

		for (int index = 0; index < jobs.length; index++) {
			
			String job = jobs[index];
			int work = millis[index];
			
			new Thread(() -> {
				try {
					Thread.sleep(work);
					System.out.printf("%s done at %d ms%n", job, 
							System.currentTimeMillis() - start);
					
				} catch (InterruptedException e) {
					
					Thread.currentThread().interrupt();
					
				} finally {
					
					// one job fewer to wait for
					openingDone.countDown();
				}
				
			}, job).start();
		}

		Thread server = new Thread(() -> {
			
			try {
				
				System.out.println("Kitchen server waiting for opening jobs...");
				
				// blocks until the count reaches zero
				openingDone.await();
				System.out.printf("Kitchen open for orders at %d ms%n", 
						System.currentTimeMillis() - start);
				
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
				
			}
			
		}, "KitchenServer");
		
		server.start();
		server.join();
	}
}