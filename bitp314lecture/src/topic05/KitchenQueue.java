package topic05;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//KitchenQueue: 
/**
 * This class represents the Kopi Corner kitchen at lunch time.
 * Two tablets (producers) put orders into a BlockingQueue, the ticket rail,
 * and three cooks (consumers) take orders from it. The queue does all the
 * locking and waiting, so there is no synchronized anywhere in this class.
 */
public class KitchenQueue {

	// Poison pill: a special ticket that tells a cook to go home
	static final KitchenOrder CLOSE = new KitchenOrder(-1, "CLOSE");
	
	// Number of dishes cooked, shared by all cooks
	static final AtomicInteger cookedCount = new AtomicInteger();

	public static void main(String[] args) throws Exception {
		
		// The ticket rail between the tablets and the cooks: holds at most 
		// 5 tickets
		BlockingQueue<KitchenOrder> queue = new ArrayBlockingQueue<>(5);
		
		// Kitchen staff: 2 tablet threads + 3 cook threads
		ExecutorService staff = Executors.newFixedThreadPool(5);
		String[] menu = {"Kopi O", "Mee goreng", "Roti bakar", "Teh tarik", 
				"Nasi lemak", "Milo ais"};

		// Producers: each tablet sends six orders (#101-106 and #201-206)
		Future<?> tablet1 = staff.submit(() -> takeOrders(queue, 101, menu));
		Future<?> tablet2 = staff.submit(() -> takeOrders(queue, 201, menu));

		// Consumers: three cooks
		for (int cookIndex = 1; cookIndex <= 3; cookIndex++) {
			
			String cookName = "Cook-" + cookIndex;
			staff.submit(() -> cook(queue, cookName));
		}

		// wait until both tablets finish
		tablet1.get();
		tablet2.get();
		for (int cookIndex = 0; cookIndex < 3; cookIndex++) {
			
			// closing time: one pill per cook
			queue.put(CLOSE);
		}

		staff.shutdown();
		staff.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("Orders sent: 12, orders cooked: " 
				+ cookedCount.get());
	}

	/**
	 * This method represents A tablet (producer): puts its orders on the 
	 * ticket rail, one every 30 ms
	 * 
	 * @param queue
	 * @param firstNumber
	 * @param menu
	 */
	public static void takeOrders(BlockingQueue<KitchenOrder> queue, 
			int firstNumber, String[] menu) {
		
		try {
			
			for (int index = 0; index < menu.length; index++) {
				
				// waits if the rail is full
				queue.put(new KitchenOrder(firstNumber + index, menu[index]));
				
				Thread.sleep(30);
			}
			
		} catch (InterruptedException e) {
			
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * This method represents A cook (consumer): takes the next ticket and 
	 * cooks it, until a CLOSE ticket arrives
	 * @param queue
	 * @param cookName
	 */
	public static void cook(BlockingQueue<KitchenOrder> queue, String cookName) {
		
		try {
			
			while (true) {
				
				// waits if the rail is empty
				KitchenOrder order = queue.take();
				if (order == CLOSE) {
					
					System.out.println(cookName + " goes home.");
					return;
				}
				
				System.out.println(cookName + " cooks #" + order.number() 
					+ " " + order.item());
				
				// cooking time
				Thread.sleep(120);
				
				cookedCount.incrementAndGet();
			}
		} catch (InterruptedException e) {
			
			Thread.currentThread().interrupt();
		}
	}
}