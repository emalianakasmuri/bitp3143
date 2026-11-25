package topic06.watchdog;


import topic06.deadlock.Resource;
import topic06.deadlock.TwoLockTask;

/**
 * The deadlock demo, now with a watchdog that detects the deadlock.
 */
public class DeadlockDemo {
	
    /**
     * Starts the two deadlocking workers and a daemon watchdog thread.
     *
     * @param args not used
     */
    public static void main(String[] args) {
    	
        Resource printer = new Resource("printer");
        Resource scanner = new Resource("scanner");

        Thread a = new Thread(new TwoLockTask(printer, scanner), "worker-A");
        Thread b = new Thread(new TwoLockTask(scanner, printer), "worker-B");

        a.start();
        b.start();

        // The watchdog is a daemon, so it does not keep the program alive on its own.
        Thread watchdog = new Thread(new DeadlockWatchdog(), "deadlock-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
    }
}