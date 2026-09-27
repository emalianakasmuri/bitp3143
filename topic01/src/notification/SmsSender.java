package notification;
/**
 * This class is a MessageSender class that delivers messages by SMS, 
 * simulating by printing the message to the console.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class SmsSender implements MessageSender {
	
	/**
     * This method simulates the SMSs sending message
     *
     * @param to   the recipient's phone number
     * @param text the message to deliver
     */
    public void send(String to, String text) {
    	
    	// A real implementation would call an SMS gateway here
        System.out.println("[SMS to " + to + "] " + text);
    }


}
