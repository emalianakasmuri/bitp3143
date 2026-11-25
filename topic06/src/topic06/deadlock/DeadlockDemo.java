package topic06.deadlock;

/**
 * Demonstrates a deadlock between two threads.
 */
public class DeadlockDemo {
    /**
     * Starts two workers that lock the printer and scanner in opposite orders.
     *
     * @param args not used
     */
    public static void main(String[] args) {
    	
        Resource printer = new Resource("printer");
        Resource scanner = new Resource("scanner");

        // worker-A locks printer then scanner; worker-B locks scanner then printer.
        Thread a = new Thread(new TwoLockTask(printer, scanner), "worker-A");
        Thread b = new Thread(new TwoLockTask(scanner, printer), "worker-B");

        a.start();
        b.start();
    }
}