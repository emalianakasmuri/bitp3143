package notification;

/**
 * This class tells customers that their payment has been received. It knows 
 * only the MessageSender interface, so the channel can change without any edit.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class PaymentNotifier {

	// The only dependency: an interface, not a concrete channel
	private final MessageSender sender;          

	/**
     * This methid creates a notifier that sends through the given channel.
     * The sender is passed in, so the notifier never chooses a channel itself.
     *
     * @param sender the channel to use for every confirmation
     */
    PaymentNotifier(MessageSender sender) {
    	this.sender = sender;
    }
    
    /**
     * This method builds the confirmation text and hands it to the sender.
     *
     * @param phone  the customer's phone number
     * @param amount the amount received in RM
     */
    void paymentReceived(String phone, double amount) {
        
    	// Format the amount to two decimal places, e.g. RM45.90
        String text = String.format("Payment of RM%.2f received. Terima kasih!", 
        		amount);
       
        // whichever channel was supplied does the delivery
        sender.send(phone, text);   
    }

}
