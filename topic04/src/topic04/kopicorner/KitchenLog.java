package topic04.kopicorner;

/*
 * Program 4.2 (part 1 of 4): KitchenLog.java
 *
 * Program description:
 *   Small helper methods shared by the Kopi Corner background jobs:
 *   printing a message tagged with the current thread's name, and sleeping
 *   without forcing every caller to handle InterruptedException.
 */
public final class KitchenLog {

    /** 
     * No objects needed: every method is static. 
     */
    private KitchenLog() {
    	
    }

    /**
     * Prints a message tagged with the name of the thread that prints it.
     *
     * @param message the text to print
     */
    public static void say(String message) {
    	
    	
        System.out.printf("%-9s %s%n", "[" + Thread.currentThread().getName() + "]", 
        	message);
    }

    /**
     * Sleeps for a while, restoring the interrupt flag if interrupted.
     *
     * @param millis how long to sleep, in milliseconds
     */
    public static void pause(long millis) {
    	
        try {
        	
        	// Pause execution
            Thread.sleep(millis * 20);
            
        } catch (InterruptedException e) {
        	
            Thread.currentThread().interrupt();
        }
    }
}