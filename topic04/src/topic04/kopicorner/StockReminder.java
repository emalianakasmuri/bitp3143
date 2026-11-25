package topic04.kopicorner;

/*
 * Program 4.2 (part 3 of 4): StockReminder.java
 *
 * Program description:
 *   Way 2: a class that implements Runnable. It reminds the cook twice to
 *   check the egg tray. A Thread object runs it.
 */
public class StockReminder implements Runnable {

    /** Reminds the cook twice to check the egg tray. */
    @Override
    public void run() {
    	
        for (int i = 1; i <= 2; i++) {
            KitchenLog.say("Check the egg tray (reminder " + i + ")");
            KitchenLog.pause(700);
        }
    }
}