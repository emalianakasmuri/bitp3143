package previous;

/*
 * Example 2.1: BatchStreamsDemo.java
 *
 * Program description:
 *   Processes a small batch of e-wallet top-up records. Each valid record is
 *   written to System.out as a CSV result line; each invalid record, and a
 *   final run summary, is written to System.err.
 *
 * What it shows:
 *   Separating results (System.out) from diagnostics (System.err) so that
 *   the operating system can send each stream to a different file.
 *
 * Class description:
 *   BatchStreamsDemo holds main and a method that checks one record.
 *   
 * How to run the program in Eclipse: 
 * 1. Right-click the project > Show In > Terminal
 * 2. Run this command java -cp bin example21.BatchStreamsDemo > results.csv 2> errors.log 
 * 3. Refresh the project. 
 */
import java.math.BigDecimal;

public class BatchStreamsDemo {

    /**
     * Entry point: processes every record, then prints a summary.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Illustrative batch: walletId,amountInRinggit
        String[] batch = {
            "W1001,50.00",
            "W1002,abc",
            "W1003,20.00",
            ",15.00",
            "W1005,100.00"
        };

        int ok = 0;
        int failed = 0;
        System.out.println("walletId,amountSen");          // CSV header (results)
        for (int i = 0; i < batch.length; i++) {
            String error = check(batch[i]);
            if (error == null) {
                String[] f = batch[i].split(",");
                long sen = new BigDecimal(f[1]).movePointRight(2).longValue();
                System.out.println(f[0] + "," + sen);       // result -> stdout
                ok++;
            } else {
                System.err.println("ERROR record " + (i + 1) + ": " + error); // diagnostic -> stderr
                failed++;
            }
        }
        System.err.println("SUMMARY processed=" + batch.length
                + " ok=" + ok + " failed=" + failed);
    }

    /**
     * Validates one record.
     *
     * @param record text in the form walletId,amount
     * @return null if valid, otherwise a description of the problem
     */
    private static String check(String record) {
        String[] f = record.split(",", -1);
        if (f.length != 2 || f[0].isBlank()) {
            return "missing wallet ID in '" + record + "'";
        }
        try {
            if (new BigDecimal(f[1]).signum() <= 0) {
                return "amount must be positive in '" + record + "'";
            }
        } catch (NumberFormatException e) {
            return "invalid amount in '" + record + "'";
        }
        return null;
    }
}
