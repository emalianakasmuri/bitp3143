package lab05.kopicorner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * TakingsReport.java (Exercise 6: TODO 6)
 *
 * Program description:
 *   At closing time, Kak Ros wants one figure: the day's takings. Sales come
 *   from three sources: tablet T1, tablet T2 and the QR phone orders. Each
 *   source is totalled by its own task in a thread pool. T2's sales list has
 *   a damaged record, and the QR order server is slow tonight. The report
 *   must not wait more than 2 seconds for any source.
 *
 * What it shows:
 *   Callable tasks that return a value, Future.get() with a time limit,
 *   ExecutionException, TimeoutException and cancel(true).
 *
 * How to run:
 *   Run as a Java application. No other program is needed.
 */
public class TakingsReport {

    /** Longest time to wait for any one source, in seconds. */
    private static final long WAIT_LIMIT_S = 2;

    /**
     * Totals every source and prints the day's takings.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        List<String> t1 = List.of("D01,3,250", "D02,4,280", "N01,2,650", "N02,1,700", "T02,2,450");
        List<String> t2 = List.of("D01,2,250", "T01,3,320", "N02,x,700");    // damaged quantity
        List<String> qr = List.of("D02,6,280", "N01,4,650");

        String[] names = {"Tablet T1", "Tablet T2", "QR phones"};
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<Integer>> futures = new ArrayList<>();
        futures.add(pool.submit(totalTask(names[0], t1, 800)));
        futures.add(pool.submit(totalTask(names[1], t2, 600)));
        futures.add(pool.submit(totalTask(names[2], qr, 5000)));      // the slow QR server
        CafeLog.say("3 totals submitted");

        int takingsSen = 0;
        int missing = 0;
        for (int i = 0; i < futures.size(); i++) {
            Future<Integer> f = futures.get(i);
            // TODO 6: collect the result of f with a time limit of WAIT_LIMIT_S seconds.
            //   a. On success, add it to takingsSen and print
            //      String.format("%-10s RM%7.2f", names[i], sen / 100.0).
            //   b. On ExecutionException, add 1 to missing and print
            //      String.format("%-10s FAILED: %s", names[i], e.getCause().getMessage()).
            //   c. On TimeoutException, add 1 to missing, cancel the task with
            //      f.cancel(true) and print
            //      String.format("%-10s LATE: no answer in %d s, task cancelled", names[i], WAIT_LIMIT_S).

        }
        pool.shutdown();

        CafeLog.say(String.format("Day's takings: RM%.2f (%d of %d sources missing)",
                takingsSen / 100.0, missing, names.length));
    }

    /**
     * Builds a task that totals one source's sales records.
     * Each record is item code, quantity and unit price in sen.
     *
     * @param source  name of the source, for messages
     * @param records the source's sales records
     * @param delayMs how long the source takes to deliver its records
     * @return a Callable that returns the source's total in sen
     */
    private static Callable<Integer> totalTask(String source, List<String> records, long delayMs) {
        return () -> {
            CafeLog.say("totalling " + source);
            try {
                Thread.sleep(delayMs);                       // fetching the records
            } catch (InterruptedException e) {
                CafeLog.say(source + " task stopped by cancel()");
                throw e;
            }
            int totalSen = 0;
            for (String r : records) {
                String[] p = r.split(",");
                try {
                    totalSen += Integer.parseInt(p[1]) * Integer.parseInt(p[2]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("bad record \"" + r + "\" from " + source);
                }
            }
            return totalSen;
        };
    }
}
