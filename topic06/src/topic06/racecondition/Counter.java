package topic06.racecondition;

/**
 * The operations every counter in this topic provides.
 * Code written against this interface can switch between the faulty
 * RequestCounter and the fixed SafeRequestCounter without other changes.
 */
public interface Counter {
	
    /** Adds one to the total. */
    void increment();

    /** Returns the current total. */
    int getCount();

}