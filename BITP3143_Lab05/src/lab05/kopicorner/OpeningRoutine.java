package lab05.kopicorner;

/*
 * OpeningRoutine.java (Exercise 3: TODO 1 and TODO 2)
 *
 * Program description:
 *   Kopi Corner opens at 8 a.m. Before Kak Ros unlocks the doors, four jobs
 *   must be done: boil the coffee urn (2.0 s), heat the toast griddle
 *   (1.5 s), send the day's menu to both counter tablets (1.0 s) and brew
 *   the first pot of kopi (1.0 s). The first pot can only be brewed once
 *   the water has boiled.
 *
 * What it shows:
 *   The three ways to give a thread its job, start(), join() from the main
 *   thread, and one thread waiting for another with join().
 *
 * How to run:
 *   Run as a Java application. No other program is needed.
 */
public class OpeningRoutine {

    /**
     * Runs the opening jobs and opens the doors when all are done.
     *
     * @param args not used
     * @throws InterruptedException if the main thread is interrupted while joining
     */
    public static void main(String[] args) throws InterruptedException {
    	
    	// Print an opening line
        CafeLog.say("Opening Kopi Corner");

        // Way 1: create a new object from a subclass of Thread
        Thread urn = new UrnHeater();                                   

        // TODO 1a: Way 2. Create a thread named "griddle" that runs a new GriddleHeater.
        Thread griddle = null;

        // TODO 1b: Way 3. Create a thread named "tablets" from a lambda that, for
        //          tablet T1 and then T2, calls CafeLog.say("Menu sent to tablet T1")
        //          (or T2) followed by CafeLog.pause(500).
        Thread tablets = null;

        Thread brew = new Thread(() -> {
            // TODO 2: wait here until the urn thread has finished.
            //         join() throws InterruptedException: catch it, restore the
            //         interrupt flag with Thread.currentThread().interrupt(), and return.

            if (urn.isAlive()) {
            	
                CafeLog.say("WARNING: brewing before the water has boiled!");
            }
            CafeLog.say("Brewing the first pot of kopi");
            CafeLog.pause(1000);
            CafeLog.say("First pot ready");
            
        }, "brew");

        // TODO 1c: start all four threads, then join all four.
        //          Only urn and brew are started and joined so far.
        urn.start();
        brew.start();

        urn.join();
        brew.join();

        CafeLog.say(String.format("Doors open after %.1f s", CafeLog.seconds()));
    }
}
