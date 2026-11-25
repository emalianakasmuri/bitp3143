package lab05.kopicorner.solution;

/*
 * FridgeMonitor.java (Exercise 4, provided complete)
 *
 * Program description:
 *   Reads the temperature of Kopi Corner's drinks fridge every 0.5 seconds
 *   and logs it. The loop never ends: the monitor is meant to run for as
 *   long as the café system is running.
 */
public class FridgeMonitor implements Runnable {

    /** Logs the fridge temperature for ever. */
    @Override
    public void run() {
        int tenths = 40;                                  // 4.0 degrees Celsius
        while (true) {
            CafeLog.say(String.format("Fridge at %.1f C", tenths / 10.0));
            tenths = tenths >= 44 ? 40 : tenths + 1;
            CafeLog.pause(500);
        }
    }
}
