package notification;
/**
 * Program 1.11b: SmsSender.java
 *
 * Class description:
 *   A MessageSender that delivers messages by SMS. For this example the
 *   delivery is simulated by printing the message to the console.
 */
public class SmsSender implements MessageSender {
	
	/**
     * Sends the message by SMS (simulated).
     *
     * @param to   the recipient's phone number
     * @param text the message to deliver
     */
    public void send(String to, String text) {
    	
    	// A real implementation would call an SMS gateway here
        System.out.println("[SMS to " + to + "] " + text);
    }


}
