package lab05.kopicorner;

/*
 * ReceiptPrinter.java (Exercise 4: TODO 3)
 *
 * Program description:
 *   At closing time, the counter printer prints copies of the day's
 *   8 receipts for Kak Ros's records. Each receipt takes 0.1 s to format
 *   (busy work on the processor) and 0.3 s to print (waiting for the
 *   printer). If the thread is interrupted, it must stop after the receipt
 *   it is working on and report how many it printed.
 *
 * What it shows:
 *   Two ways a thread notices interrupt(): isInterrupted() between pieces
 *   of work, and an InterruptedException thrown by sleep().
 */
public class ReceiptPrinter implements Runnable {

    /** Number of receipts to print. */
    private static final int RECEIPTS = 8;

    /** Prints the receipts, stopping early if interrupted. */
    @Override
    public void run() {
    	
        int printed = 0;
        
        // TODO 3: make the printer stop politely when it is interrupted.
        //   a. At the start of each round, check Thread.currentThread().isInterrupted().
        //      If it is set, print "Interrupt noticed before receipt <no>" and leave the loop.
        //   b. Wrap the loop in try. Catch InterruptedException there instead of inside
        //      the loop, and print "Interrupted while printing receipt <printed + 1>".
        //   c. Move the last message into a finally block.
        for (int no = 1; no <= RECEIPTS; no++) {
            
        	format();
            
        	try {
            	
            	 // waiting for the printer
                Thread.sleep(300);
                
            } catch (InterruptedException e) {
            	
                // ignored: the printer carries on regardless
            }
        	
            printed++;
            CafeLog.say("Printed receipt " + no + " of " + RECEIPTS);
        }
        
        CafeLog.say(printed + " receipts printed; the rest will print tomorrow");
    }

    /** 
     * Formats one receipt: 0.1 s of busy work on the processor. 
     */
    private static void format() {
    	
        long end = System.nanoTime() + 100_000_000L;
        
        while (System.nanoTime() < end) {
        	
            // laying out the receipt text
        }
    }
}
