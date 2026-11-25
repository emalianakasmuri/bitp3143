package topic04.kopicorner;

/*
 * Program 4.2 (part 2 of 4): NumberCaller.java
 *
 * Program description:
 *   Way 1: a subclass of Thread that overrides run(). It calls three order
 *   numbers at the Kopi Corner counter, half a second apart.
 */

public class NumberCaller extends Thread {

    /**
     * Creates the caller thread with a readable name.
     */
    public NumberCaller() {
    	
        super("caller");
    }

    /** Calls three order numbers, half a second apart. */
    @Override
    public void run() {
    	
        for (int no = 101; no <= 103; no++) {
            KitchenLog.say("Order " + no + " ready at the counter");
            KitchenLog.pause(500);
        }
    }
}
