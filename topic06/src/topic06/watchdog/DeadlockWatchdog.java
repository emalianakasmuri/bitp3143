package topic06.watchdog;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * A background task that checks for deadlocked threads every two seconds.
 * It detects a deadlock and reports it; it does not prevent one.
 */
public class DeadlockWatchdog implements Runnable {
	
    /** The JVM's thread management bean, the same source a thread dump uses. */
    private final ThreadMXBean threads = ManagementFactory.getThreadMXBean();

    /** Checks for a deadlock, reports the threads involved, and ends the demo. */
    @Override
    public void run() {
    	
        while (true) {
        	
            // Returns the ids of threads caught in a lock cycle, or null if none.
            long[] ids = threads.findDeadlockedThreads();
            if (ids != null) {
            	
                System.out.println("DEADLOCK DETECTED between " + ids.length + " threads:");
                ThreadInfo[] infos = threads.getThreadInfo(ids, true, true);
                
                for (ThreadInfo info : infos) {
                	
                    System.out.printf("  %s is %s, waiting for %s held by %s%n",
                            info.getThreadName(), info.getThreadState(),
                            info.getLockName(), info.getLockOwnerName());
                }
                // End the demo. A real system would raise an alert instead.
                System.exit(1);
            }
            
            try {
            	
                Thread.sleep(2000);
                
            } catch (InterruptedException e) {
            	
                return;
            }
        }
    }
}