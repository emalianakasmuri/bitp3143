package topic05;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the kitchen's order board, shared by all cooks.
 * The takeNext() method lets a cook claim the next free ticket, which is
 * NOT a thread-safe method.
 */

public class OrderBoard {
	
	private final List<Order> orders = new ArrayList<>();

	/**
	 * This method allow the counter staff pin a new ticket on the board
	 * @param order
	 */
	public void add(Order order) {
		
		orders.add(order);
	}
	
	/**
	 * This method let s cook looks for the first ticket nobody has taken 
	 * and claims it
	 * 
	 * @return
	 */

	public Order takeNext() {
		
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
