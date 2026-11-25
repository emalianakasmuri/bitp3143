package topic06.visibilityproblem;


/**
 * A worker that spins until another thread tells it to stop.
 * It contains a visibility bug: the stop flag is not volatile.
 */
public class SpinWorker implements Runnable {
	
    /**
     * The stop flag, written by the main thread and read by the worker thread.
     * Because it is not volatile, the worker may never see the change.
     */
    private boolean running = true;

    /** 
     * Asks the worker to stop. Called from the main thread. 
     */
    public void stop() {
    	
        running = false;
    }

    /** 
     * Loops while the flag is true, then reports how many passes it made. 
     */
    @Override
    public void run() {
    	
        long loops = 0;
        while (running) {
        	
            loops++;
        }
        
        System.out.println("Worker stopped after " + loops + " loops");
    }
}