package topic04.kopicorner;

/*
 * Program 4.10: VirtualPhones.java
 *
 * Program description:
 *   During the campus food festival, Kak Ros expects thousands of phones to
 *   be waiting on the QR order server at the same moment. Each simulated
 *   task waits 0.5 seconds, as if waiting for a reply. The program runs 1,000
 *   such tasks on a fixed pool of 100 platform threads, then 10,000 tasks
 *   with one virtual thread each.
 *
 * What it shows:
 *   Executors.newVirtualThreadPerTaskExecutor() (Java 21), and why virtual
 *   threads suit work that spends most of its time waiting.
 *
 * How to run:
 *   Run as a Java application. Requires Java 21 or later.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualPhones {

    /**
     * Times both executors and prints the results.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        
    	time("1,000 tasks, fixed pool of 100 platform threads", 
        		Executors.newFixedThreadPool(100), 1_000);
        time("10,000 tasks, one virtual thread per task", 
        		Executors.newVirtualThreadPerTaskExecutor(), 10_000);
    }

    /**
     * Submits waiting tasks to an executor and measures how long they all take.
     *
     * @param label    description printed with the result
     * @param executor the executor to use; closed when the method ends
     * @param tasks    number of tasks to submit
     */
    private static void time(String label, ExecutorService executor, int tasks) {
        
    	long start = System.nanoTime();
        
    	// ExecutorService is AutoCloseable since Java 19: close() 
    	// waits for every task to finish
        try (executor) {
        	
            for (int i = 0; i < tasks; i++) {
            	
                executor.submit(() -> {
                	
                	// waiting for the network
                    Thread.sleep(500);             
                    return null;
                });
            }
        }
        
        System.out.printf("%-50s %5.1f s%n", label, (System.nanoTime() - start) / 1e9);
    }
}