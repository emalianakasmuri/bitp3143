package notification;

/**
 * This program  sends the same payment confirmation first by SMS and then by 
 * WhatsApp,using one PaymentNotifier class that never changes.
 *
 * The program demonstrate the following:-
 * 
 *   - Low coupling: PaymentNotifier works with any MessageSender.
 *   - A new channel is added by writing one new class, with no change to
 *     existing code.
 *     
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class LowCouplingDemo {
	
	/**
     * Main entry point to the program.
     * 
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
