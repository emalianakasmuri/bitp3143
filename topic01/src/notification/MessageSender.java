package notification;

/**
 * Program 1.11a: MessageSender.java
 *
 * Interface description:
 *   The contract for any channel that can deliver a text message.
 *   PaymentNotifier depends only on this interface, never on a concrete
 *   channel, which keeps the coupling between them low.
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
