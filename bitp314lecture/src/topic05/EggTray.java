package topic05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents the Kopi Corner egg tray, shared by Kak Ros and the 
 * cooks.  A ReentrantLock guards the egg count, so only one person uses the 
 * tray at a time.
 */
public class EggTray {
	
	// the tray's lock
	private final ReentrantLock lock = new ReentrantLock();
	
	// eggs on the tray
	private int eggs = 30;

	// 
	/**
	 * This method allows Kak Ros restocks the tray. 
	 * Restocking is slow: she counts the new tray while holding the lock, 
	 * so everyone else must wait. 
	 */
	public void restock(int newEggs) {
		
		// wait as long as needed
		lock.lock();
		
		try {
			
			// counting the new tray
			EggTrayLock.pause(300);
			
			eggs += newEggs;
			
			System.out.println("Kak Ros restocked. Eggs now: " + eggs);
			
		} finally {
			
			// always released
			lock.unlock();
		}
	}

	/**
	 * This method allow A cook takes eggs for a dish, but waits at most 100 ms 
	 * for the tray.
	 * Returns false if the tray was busy or there were not enough eggs.
	 * @param needed
	 * @return
	 * @throws InterruptedException
	 */
	public boolean tryUseEggs(int needed) throws InterruptedException {
		
		if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
			
			try {
				
				if (eggs < needed) {
					
					return false;
				}
				
				eggs -= needed;
				
				return true;
				
			} finally {
				
				lock.unlock();
			}
		}
		
		// could not get the tray in time
		return false;
	}
}