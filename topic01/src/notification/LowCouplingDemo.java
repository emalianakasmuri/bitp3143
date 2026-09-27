package notification;

/**
 * Program 1.11e: LowCouplingDemo.java
 *
 * Program description:
 *   Sends the same payment confirmation first by SMS and then by WhatsApp,
 *   using one PaymentNotifier class that never changes.
 *
 * What it shows:
 *   - Low coupling: PaymentNotifier works with any MessageSender.
 *   - A new channel is added by writing one new class, with no change to
 *     existing code.
 *
 */
public class LowCouplingDemo {
	
	/**
     * Main entry point to the program.
     * Creates a notifier for each channel and sends the same confirmation.
     */
    public static void main(String[] args) {
    	
    	// Plug the SMS channel into the notifier
        PaymentNotifier viaSms = new PaymentNotifier(new SmsSender());
        viaSms.paymentReceived("012-3456789", 45.90);

        // Switching channel needs NO change inside PaymentNotifier
        PaymentNotifier viaWhatsApp = new PaymentNotifier(new WhatsAppSender());
        viaWhatsApp.paymentReceived("012-3456789", 45.90);
    }


}
