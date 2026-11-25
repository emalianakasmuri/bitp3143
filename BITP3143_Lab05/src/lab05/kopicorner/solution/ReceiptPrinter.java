package lab05.kopicorner.solution;

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
        try {
            for (int no = 1; no <= RECEIPTS; no++) {
                if (Thread.currentThread().isInterrupted()) {
                    CafeLog.say("Interrupt noticed before receipt " + no);
                    break;
                }
                format();
                Thread.sleep(300);                         // waiting for the printer
                printed++;
                CafeLog.say("Printed receipt " + no + " of " + RECEIPTS);
            }
        } catch (InterruptedException e) {
            CafeLog.say("Interrupted while printing receipt " + (printed + 1));
        } finally {
            CafeLog.say(printed + " receipts printed; the rest will print tomorrow");
        }
    }

    /** Formats one receipt: 0.1 s of busy work on the processor. */
    private static void format() {
        long end = System.nanoTime() + 100_000_000L;
        while (System.nanoTime() < end) {
            // laying out the receipt text
        }
    }
}
