package lab05.kopicorner.solution;

/*
 * GriddleHeater.java (Exercise 3, provided complete)
 *
 * Program description:
 *   Way 2 of giving a thread its job: a class that implements Runnable.
 *   It heats the toast griddle, which takes 1.5 seconds. A Thread object
 *   must be created to run it.
 */
public class GriddleHeater implements Runnable {

    /** Heats the griddle. */
    @Override
    public void run() {
        CafeLog.say("Heating the toast griddle");
        CafeLog.pause(1500);
        CafeLog.say("Griddle hot");
    }
}
