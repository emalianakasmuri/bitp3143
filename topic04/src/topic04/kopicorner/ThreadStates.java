package topic04.kopicorner;

/*
 * Program 4.3: ThreadStates.java
 *
 * Program description:
 *   Follows one Kopi Corner cook thread through its life: created, running,
 *   sleeping while the noodles boil, and finished. The main thread checks
 *   the cook's state at each stage with getState().
 *
 * What it shows:
 *   The thread states NEW, RUNNABLE, TIMED_WAITING and TERMINATED, and
 *   isAlive().
 *
 * How to run:
 *   Run as a Java application.
 */
public class ThreadStates {

    /**
     * Creates the cook thread and reports its state at four moments.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while sleeping or joining
     */
    public static void main(String[] args) throws InterruptedException {
    	
    	// Creating threads using lambda. Thread name is 'ccok' 
        Thread cook = new Thread(() -> {
        	
        	// 0.2 s of busy work: chopping
            long end = System.nanoTime() + 200_000_000L;   
            while (System.nanoTime() < end) {
                // the thread is using the processor here
            }
            
            try {
            	// waiting for the noodles to boil
                Thread.sleep(1000);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                
            }
        }, "cook");

        report(cook, "after new Thread(...)");
        cook.start();
        Thread.sleep(50);
        report(cook, "while chopping");
        Thread.sleep(500);
        report(cook, "while the noodles boil");
        cook.join();
        report(cook, "after join()");
    }

    /**
     * Prints the state of a thread with a label.
     *
     * @param t     the thread to inspect
     * @param label when the check is made
     */
    private static void report(Thread t, String label) {
    	
        System.out.printf("%-24s state = %-13s alive = %b%n", label, t.getState(), 
        	t.isAlive());
    }
}