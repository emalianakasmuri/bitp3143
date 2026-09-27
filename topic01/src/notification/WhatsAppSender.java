package notification;

/**
 * Program 1.11c: WhatsAppSender.java
 *
 * Class description:
 *   A MessageSender that delivers messages by WhatsApp. For this example the
 *   delivery is simulated by printing the message to the console.
 */
public class WhatsAppSender implements MessageSender {

	
	/**
     * Sends the message by WhatsApp (simulated).
     *
     * @param to   the recipient's phone number
     * @param text the message to deliver
     */
    public void send(String to, String text) {
    	
    	// A real implementation would call a messaging API here
        System.out.println("[WhatsApp to " + to + "] " + text);
    }

}
