package topic05;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//ItemSalesCounts: 
/**
 * This class allows Kak Ros's closing report of drinks sold at Kopi Corner.
 * Four order-handler threads replay the day's sales. The report is counted
 * twice: unsafely with HashMap and int, then safely with ConcurrentHashMap
 * and AtomicInteger.
 */
public class ItemSalesCounts {

	static final String[] DRINKS = {"Kopi O", "Teh tarik", "Milo ais"};
	
	// order-handler threads
	static final int HANDLERS = 4;
	
	// cups each handler records
	static final int CUPS_EACH = 30_000;

	public static void main(String[] args) throws InterruptedException {
		
		// Unsafe version
		Map<String, Integer> unsafeCounts = new HashMap<>();
		int[] unsafeTotal = {0};
		runHandlers(drink -> {
			
			unsafeCounts.put(drink, unsafeCounts.getOrDefault(drink, 0) + 1);
			unsafeTotal[0]++;
		});
		System.out.println("HashMap + int:              " + unsafeCounts 
				+ "  total=" + unsafeTotal[0]);

		// Safe version
		Map<String, Integer> safeCounts = new ConcurrentHashMap<>();
		AtomicInteger safeTotal = new AtomicInteger();
		runHandlers(drink -> {
			
			// atomic read-add-write per key
			safeCounts.merge(drink, 1, Integer::sum);
			
			// atomic ++
			safeTotal.incrementAndGet();
		});
		System.out.println("ConcurrentHashMap + Atomic: " + safeCounts 
				+ "  total=" + safeTotal.get());
		System.out.println("Expected:                   40000 per drink, "
				+ "total=120000");
	}

	// 
	/**
	 * This method starts the four order handlers. Each records CUPS_EACH cups,
	 * spread evenly across the drinks, using the given SaleAction.
	 * @param action
	 * @throws InterruptedException
	 */
	public static void runHandlers(SaleAction action) throws InterruptedException {
		
		Thread[] handlers = new Thread[HANDLERS];
		
		for (int h = 0; h < HANDLERS; h++) {
			
			handlers[h] = new Thread(() -> {
				for (int i = 0; i < CUPS_EACH; i++) {
					action.record(DRINKS[i % DRINKS.length]);
				}
			}, "Handler-" + (h + 1));
			handlers[h].start();
		}
		
		for (Thread t : handlers) {
			
			t.join();
		}
	}
}