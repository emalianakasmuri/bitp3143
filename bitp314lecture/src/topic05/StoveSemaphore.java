package topic05;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class demonstrate sempahore using three cooks share two stoves
 */
public class StoveSemaphore {

	// two permits = two stoves
	static final Semaphore stoves = new Semaphore(2);
	
	static final AtomicInteger inUse = new AtomicInteger();
	static final AtomicInteger maxInUse = new AtomicInteger();

	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService cooks = Executors.newFixedThreadPool(3);
		String[] orders = {"#101 Mee goreng", "#102 Nasi goreng", 
			"#103 Telur dadar", "#104 Kuey teow", "#105 Mee goreng", 
			"#106 Bihun goreng"};

		for (String order : orders) {
			cooks.submit(() -> fry(order));
		}
		
		cooks.shutdown();
		cooks.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("Most stoves in use at once: " + maxInUse.get());
	}

	public  static void fry(String order) {
		
		try {
			
			// wait for a free stove
			stoves.acquire();
			
			try {
				
				int now = inUse.incrementAndGet();
				maxInUse.accumulateAndGet(now, Math::max);
				System.out.println(Thread.currentThread().getName().
						replace("pool-1-thread-", "Cook-")
						+ " fries " + order + " (stoves in use: " + now + ")");
				
				Thread.sleep(200);
				
				inUse.decrementAndGet();
				
			} finally {
				
				// give the stove back
				stoves.release();
			}
			
		} catch (InterruptedException e) {
			
			Thread.currentThread().interrupt();
		}
	}
}