package topic06.ordertrace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * (Program 6.3, part 2)
 * 
 * This class traces every post to find out where the missing order went 
 */
public class TracedLunchRush {

    static final ConcurrentLinkedQueue<OrderEvent> trace = new ConcurrentLinkedQueue<>();

    /**
     * Main entry point to the application.
     * 
     * The program repeats trials until orders go missing, then explains the first 
     * missing order.
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
    	
    	// Run up to 50 lunch rushes, stopping at the first one that loses orders
        for (int trial = 1; trial <= 50; trial++) {
        	
        	// Start each trial with an empty trace
            trace.clear();
            // Two tablets post 50,000 orders each, 100,000 in total
            List<Integer> board = runTrial(50000);
            
            if (board.size() == 100000) {
            	
                // no loss this time, try again
                continue;
            }
            
            // Orders were lost: copy the board into a set for quick lookup
            Set<Integer> onBoard = new HashSet<>(board);
            int missing = -1;
            // Find missing order
            for (int orderNo = 1; orderNo <= 100000 && missing < 0; orderNo++) {
            	
                // first missing order number
                if (!onBoard.contains(orderNo)) missing = orderNo;
            }
            
            // Report the number of missing orders vanished and which one went first
            System.out.printf("Trial %d: %d orders missing. First missing: #%d%n",
                    trial, 100000 - board.size(), missing);
            explain(missing);
            
            return;
        }
        System.out.println("No orders lost in 50 trials.");
    }

    /**
     * This method prints every event that saw the same board size as the missing order
     * @param missing
     */
    static void explain(int missing) {
    	
        OrderEvent lost = null;
        
        for (OrderEvent event : trace) {
            if (event.orderNo() == missing) lost = event;
        }
        
        System.out.println("Events that saw the same board size:");
        for (OrderEvent e : trace) {
            if (e.boardSizeSeen() == lost.boardSizeSeen()) {
                System.out.printf("  %-8s posted #%-6d  board size seen: %d%n",
                        e.thread(), e.orderNo(), e.boardSizeSeen());
            }
        }
    }
    
    /**
     * This method records one post in the trace: time, thread, order number and the 
     * board size seen
     * 
     * @param orderNo
     * @param boardSizeSeen
     */
    static void log(int orderNo, int boardSizeSeen) {
        trace.add(new OrderEvent(System.nanoTime(), Thread.currentThread().getName(), orderNo, boardSizeSeen));
    }

    // Runs one traced lunch rush and returns the board
    static List<Integer> runTrial(int ordersEach) throws InterruptedException {
        List<Integer> board = new ArrayList<>();
        CountDownLatch startGate = new CountDownLatch(1);
        Thread[] tablets = new Thread[2];
        for (int t = 0; t < 2; t++) {
            int first = t * ordersEach + 1;
            tablets[t] = new Thread(() -> {
                try {
                    startGate.await();
                    for (int n = first; n < first + ordersEach; n++) {
                        // what this tablet sees
                        int seen = board.size();
                        board.add(n);
                        // record it, cheaply
                        log(n, seen);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Tablet-" + (t + 1));
            tablets[t].start();
        }
        startGate.countDown();
        for (Thread tablet : tablets) tablet.join();
        return board;
    }
}