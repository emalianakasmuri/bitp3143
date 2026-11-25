package topic04.kopicorner;

/*
 * Program 4.9: MenuChecker.java
 *
 * Program description:
 *   Each morning Kopi Corner's kitchen server loads the menu, which is kept
 *   in three section files: drinks, noodles and toast. Each section is
 *   checked by a separate task that counts its valid lines (format:
 *   item,price), as in Topic 2. Checking takes time, as if each file were
 *   read from a slow disk. One section file is damaged.
 *
 * What it shows:
 *   Callable tasks that return a value, Future.get() to collect each result,
 *   and how an exception thrown inside a task reaches the caller as an
 *   ExecutionException.
 *
 * How to run:
 *   Run as a Java application.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MenuChecker {

    /**
     * Submits one check per menu section and prints each result as it is collected.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while waiting for a result
     */
    public static void main(String[] args) throws InterruptedException {
        
    	List<String> drinks = List.of(
    			"Kopi O,2.50", "Teh tarik,3.00", "Milo ais,abc", "Kopi susu,3.20");
        List<String> noodles = List.of("Mee goreng,7.50", "Mee kari,9.00");
        // damaged: a line with no comma
        List<String> toast = List.of("Roti bakar,3.50", "#####");   

        ExecutorService pool = Executors.newFixedThreadPool(3);
        long start = System.nanoTime();

        List<Future<Integer>> results = new ArrayList<>();
        results.add(pool.submit(checkTask("Drinks", drinks)));
        // submit() returns at once with a Future
        results.add(pool.submit(checkTask("Noodles", noodles)));
        results.add(pool.submit(checkTask("Toast", toast)));
        System.out.println("[main] 3 checks submitted; main is free to do other work");

        String[] names = {"Drinks", "Noodles", "Toast"};
        for (int i = 0; i < results.size(); i++) {
            
        	try {
        		// blocks until that task has finished
        		int valid = results.get(i).get();         
        		
                System.out.println("[main] " + names[i] + ": " + valid + " valid items");
            } catch (ExecutionException e) {
            	
                // The task threw an exception; get() wraps it, 
            	// and getCause() returns the original
                System.out.println("[main] " + names[i] + ": check failed - " 
                		+ e.getCause().getMessage());
            }
        }
        System.out.printf("[main] All checks done after %.1f s%n", 
        		(System.nanoTime() - start) / 1e9);
        pool.shutdown();
    }

    /**
     * Builds a task that counts valid lines in one menu section.
     *
     * @param section name of the section, for messages
     * @param lines   the section's lines
     * @return a Callable that returns the number of valid items
     */
    private static Callable<Integer> checkTask(String section, List<String> lines) {
        
    	return () -> {
        	
    		// reading the section file from a slow disk
            Thread.sleep(1000);                        
            
            int valid = 0;
            
            for (String line : lines) {
            
            	String[] parts = line.split(",");
                
            	if (parts.length != 2) {
                    throw new IllegalArgumentException(""
                    		+ "damaged line \"" + line + "\" in " + section);
                }
                
            	try {
                    
            		Double.parseDouble(parts[1]);
                    valid++;
                    
                } catch (NumberFormatException e) {
                    // an invalid price: skip this item, as in Topic 2
                }
            }
            
            return valid;
        };
    }
}