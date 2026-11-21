package topic08.program81;

/**
 * Program 8.1: 
 * How much parallel hardware does this machine offer?
 * 
 * @author Emaliana Kasmuri, FTMK, UTeM
 */

public class CoreInfo {
	
	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Get the number of available cores
        Runtime runtime = Runtime.getRuntime();
        int cores = runtime.availableProcessors();
        long maxMemoryMb = runtime.maxMemory() / (1024 * 1024);

        System.out.println("Logical processors available to the JVM: " + cores);
        System.out.println("Maximum heap memory (MB): " + maxMemoryMb);
        System.out.println("Operating system: " + System.getProperty("os.name"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
    }

}
