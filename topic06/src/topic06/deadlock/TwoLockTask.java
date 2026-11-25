package topic06.deadlock;


/**
 * A task that locks two resources, one after the other.
 * Two tasks that take the same resources in opposite orders deadlock.
 */
public class TwoLockTask implements Runnable {
	
    /** The resource this task locks first. */
    private final Resource first;
    
    /** The resource this task locks second, while still holding the first. */
    private final Resource second;

    /**
     * Creates a task that locks the resources in the given order.
     *
     * @param first  the resource to lock first
     * @param second the resource to lock second
     */
    public TwoLockTask(Resource first, Resource second) {
    	
        this.first = first;
        this.second = second;
    }

    /** Takes the first lock, pauses, then waits for the second lock. */
    @Override
    public void run() {
    	
        String me = Thread.currentThread().getName();
        
        // first lock
        synchronized (first) { // lock is acquired
        	
            System.out.println(me + " holds " + first.getName());
            
            // Pausing while holding the first lock widens the window,
            // so the deadlock appears on every run.
            pause(100);
            
            System.out.println(me + " wants " + second.getName());
            
            // second lock
            synchronized (second) { // lock is acquired
            	
                System.out.println(me + " holds both resources");
            
            } // lock is acquired
        
        } // lock is released
    }

    /**
     * Sleeps for the given time and keeps the interrupt flag if interrupted.
     *
     * @param millis the pause length in milliseconds
     */
    private void pause(long millis) {
    	
        try {
        	
            Thread.sleep(millis);
            
        } catch (InterruptedException e) {
        	
            Thread.currentThread().interrupt();
        }
    }
}