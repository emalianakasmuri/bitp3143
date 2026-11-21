package topic05;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Kopi Corner order board, made safe with a private 
 * lock. A cook must hold boardLock to check and claim a ticket, so no two cooks
 * can claim the same ticket.
 */
public class LockedOrderBoard {

	private final List<Order> orders = new ArrayList<>();
	
	// the board's own lock
	private final Object boardLock = new Object();

	/**
	 * This method allow the counter staff pin a new ticket on the board.
	 * 
	 * @param order
	 */
	public void add(Order order) {
		
		synchronized (boardLock) {
			
			orders.add(order);
		}
	}

	/**
	 * This method allow a cook claims the next free ticket; check and act 
	 * happen as one step
	 * @return
	 */
	public Order takeNext() {
		
		synchronized (boardLock) {
			
			for (Order order : orders) {
				
				// check: is it free?
				if (!order.taken) {
					
					// cook reads the ticket
					DoubleTakeDemo.pause(5);
					
					// act: claim it
					order.taken = true;
					
					return order;
				}
			}
			
			// nothing left
			return null;
		}
	}
}