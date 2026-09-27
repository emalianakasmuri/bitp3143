package notification;

/**
 * This class is MessageSender that simulates the Whatsapp delivery messages by 
 * by printing the message to the console.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development     
 */
public class WhatsAppSender implements MessageSender {

	
	/**
     * This method simulates the WhatsApp message sending process.
     *
     * @param to   the recipient's phone number
     * @param text the message to deliver
     */
    public void send(String to, String text) {
    	
    	// A real implementation would call a messaging API here
        System.out.println("[WhatsApp to " + to + "] " + text);
    }

}
