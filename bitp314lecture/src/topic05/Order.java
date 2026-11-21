package topic05;

/**
 * This class represents one customer ticket on the Kopi Corner order board, 
 * e.g. #101 Kopi O.
 * 
 * The attribute taken records whether a cook has already claimed the ticket.
 */

public class Order {
	
	// ticket number, e.g. 101
	final int number;
	
	// what the customer ordered
	final String item;
	
	// true once a cook claims it
	boolean taken = false;

	Order(int number, String item) {
		
		this.number = number;
		this.item = item;
	}
}