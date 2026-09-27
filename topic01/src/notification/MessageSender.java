package notification;

/** *
 * This interface define contract for any channel that can deliver a text 
 * message.
 * 
 * The PaymentNotifier class depends only on this interface, never on a concrete
 * channel, which keeps the coupling between them low.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public interface MessageSender {

	/**
     * Delivers a message to a recipient.
     *
     * @param to   the recipient's phone number
     * @param text the message to deliver
     */
	void send(String to, String text);
}
