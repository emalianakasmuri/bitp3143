package lab05.kopicorner;

/*
 * ClosingTime.java (Exercise 4: TODO 4)
 *
 * Program description:
 *   At closing time, the fridge monitor is running and the counter printer
 *   is printing the day's receipts. After 1.5 seconds Kak Ros switches the
 *   lights off: the main thread interrupts the printer, waits at most
 *   2 seconds for it, and ends. The program should then finish by itself.
 *
 * What it shows:
 *   Thread states with getState(), interrupt(), join() with a time limit,
 *   and daemon threads with setDaemon().
 *
 * How to run:
 *   Run as a Java application. No other program is needed.
 */
public class ClosingTime {

    /**
     * Starts the monitor and the printer, then closes the café.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
    	
        Thread monitor = new Thread(new FridgeMonitor(), "fridge");
        Thread printer = new Thread(new ReceiptPrinter(), "printer");

        // TODO 4a: mark the fridge monitor as a daemon thread.


        report(printer, "before start()");
        monitor.start();
        printer.start();
        CafeLog.pause(50);
        report(printer, "while formatting");
        CafeLog.pause(200);
        report(printer, "while printing");

        // 1.5 s after opening: lights off
        CafeLog.pause(1250);                              
        CafeLog.say("Lights off: interrupting the printer");
        printer.interrupt();

        // TODO 4b: wait at most 2 seconds for the printer, then report its state
        //          with the label "after join(2000)".


        CafeLog.say("Main thread ends. Fridge monitor is a daemon? " + monitor.isDaemon());
    }

    /**
     * Prints the state of a thread with a label.
     *
     * @param t     the thread to inspect
     * @param label when the check is made
     */
    private static void report(Thread t, String label) {
    	
        CafeLog.say(String.format("printer %-18s state = %-13s alive = %b", 
        		label, t.getState(), t.isAlive()));
    }
}
