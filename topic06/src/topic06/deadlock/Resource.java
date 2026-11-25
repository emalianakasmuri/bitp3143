package topic06.deadlock;


/**
 * A shared device, such as a printer or a scanner.
 * Each Resource object is also used as a lock with synchronized.
 */
public class Resource {
	
    /** The device name, used in log messages. */
    private final String name;

    /**
     * Creates a resource.
     *
     * @param name the device name
     */
    public Resource(String name) {
    	
        this.name = name;
    }

    /** @return the device name */
    public String getName() {
    	
        return name;
    }
}