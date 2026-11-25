package lab05.kopicorner.solution;

/*
 * UrnHeater.java (Exercise 3, provided complete)
 *
 * Program description:
 *   Way 1 of giving a thread its job: a subclass of Thread that overrides
 *   run(). It boils the water in Kopi Corner's coffee urn, which takes
 *   2 seconds.
 */
public class UrnHeater extends Thread {

    /** Creates the urn thread with a readable name. */
    public UrnHeater() {
        super("urn");
    }

    /** Boils the water. */
    @Override
    public void run() {
        CafeLog.say("Heating the coffee urn");
        CafeLog.pause(2000);
        CafeLog.say("Water boiled");
    }
}
