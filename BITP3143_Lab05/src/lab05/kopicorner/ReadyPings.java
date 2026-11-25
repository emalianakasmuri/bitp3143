package lab05.kopicorner;

import java.util.ArrayList;
import java.util.List;

/*
 * ReadyPings.java (Exercise 7: TODO 7)
 *
 * Program description:
 *   During the campus food festival, every order at Kopi Corner comes from
 *   a QR phone. When an order is ready, the kitchen server pings the
 *   customer's phone and waits 0.5 seconds for it to acknowledge. The
 *   program pings 1,000 phones with one platform thread each, then
 *   10,000 phones with one virtual thread each. Each thread records its
 *   phone's acknowledgement in its own slot of an array.
 *
 * What it shows:
 *   Thread.ofPlatform() and Thread.ofVirtual() (Java 21), isVirtual(), and
 *   why virtual threads suit work that spends most of its time waiting.
 *
 * How to run:
 *   Run as a Java application. Requires Java 21 or later.
 */
public class ReadyPings {

    /** How long a phone takes to acknowledge a ping, in milliseconds. */
    private static final long ACK_MS = 500;

    /**
     * Runs both experiments.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while joining
     */
    public static void main(String[] args) throws InterruptedException {
        pingWithPlatformThreads(1_000);
        pingWithVirtualThreads(10_000);
    }

    /**
     * Pings every phone, one platform thread per phone.
     *
     * @param phones number of phones to ping
     * @throws InterruptedException if interrupted while joining
     */
    private static void pingWithPlatformThreads(int phones) throws InterruptedException {
        boolean[] acked = new boolean[phones];          // slot i belongs to phone i only
        List<Thread> threads = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < phones; i++) {
            int phone = i;
            threads.add(Thread.ofPlatform().name("ping-", 0).start(() -> acked[phone] = ping()));
        }
        for (Thread t : threads) {
            t.join();
        }
        report("platform", phones, acked, threads.get(0), start);
    }

    /**
     * Pings every phone, one virtual thread per phone.
     *
     * @param phones number of phones to ping
     * @throws InterruptedException if interrupted while joining
     */
    private static void pingWithVirtualThreads(int phones) throws InterruptedException {
        boolean[] acked = new boolean[phones];
        List<Thread> threads = new ArrayList<>();
        long start = System.nanoTime();
        // TODO 7a: start one virtual thread per phone, named "vping-0", "vping-1", ...
        //          Each thread stores ping() in its own slot of acked. Keep every thread in the list.
        //          Use pingWithPlatformThreads() above as your model.

        // TODO 7b: wait for every thread in the list to finish.

        if (!threads.isEmpty()) {                       // skips the report until TODO 7 is done
            report("virtual", phones, acked, threads.get(0), start);
        }
    }

    /**
     * Pings one phone and waits for its acknowledgement.
     *
     * @return true when the phone acknowledged
     */
    private static boolean ping() {
        try {
            Thread.sleep(ACK_MS);                       // waiting for the phone's reply
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Prints one line of results.
     *
     * @param kind   the kind of thread used
     * @param phones number of phones pinged
     * @param acked  which phones acknowledged
     * @param first  the first thread started, as an example
     * @param start  the time at which the experiment started
     */
    private static void report(String kind, int phones, boolean[] acked, Thread first, long start) {
        int count = 0;
        for (boolean a : acked) {
            if (a) {
                count++;
            }
        }
        System.out.printf("%-8s threads: %,6d of %,6d phones acknowledged in %.2f s (first thread: %s, virtual = %b)%n",
                kind, count, phones, (System.nanoTime() - start) / 1e9, first.getName(), first.isVirtual());
    }
}
