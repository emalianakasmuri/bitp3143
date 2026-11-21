package lab03.kopicorner;

/** 
 * Checked exception for an order the counter must reject. 
 * 
 */
public class InvalidOrderException extends Exception {
    
	public InvalidOrderException(String message) {
        super(message);
    }
}
