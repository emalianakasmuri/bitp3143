package topic06.visibilityproblem;


/**
 * Demonstrates the visibility problem in SpinWorker.
 */
public class VisibilityDemo {
	
    /**
     * Starts the worker, asks it to stop after one second, and checks
     * whether it actually stopped.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted while waiting
     */
    public static void main(String[] args) throws InterruptedException {
    	
        SpinWorker worker = new SpinWorker();
        Thread t = new Thread(worker, "spin-worker");
        
        // A daemon thread lets the program exit even if the worker never stops.
        t.setDaemon(true);
        t.start();

        Thread.sleep(1000);
        worker.stop();
        System.out.println("Main thread asked the worker to stop");

        // Wait at most three seconds for the worker to finish.
        t.join(3000);
        if (t.isAlive()) {
        	
            System.out.println("Worker is still running: it never saw the stop signal");
        }
    }
}