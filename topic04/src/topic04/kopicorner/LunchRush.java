package topic04.kopicorner;

/*
 * Program 4.6: LunchRush.java
 *
 * Program description:
 *   Simulates the 1 p.m. lunch rush: both Kopi Corner counter tablets connect
 *   to the kitchen server at the same moment and send three orders each.
 *   Each tablet runs on its own thread, and the program prints when each
 *   tablet finished.
 *
 * What it shows:
 *   Threads on the client side too, and a simple way to measure how long
 *   each client had to wait.
 *
 * How to run:
 *   Start ConcurrentKitchenServer (Program 4.5) first, then run this program.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class LunchRush {

    /** Time at which the rush started. */
    private static final long START = System.nanoTime();

    /**
     * Starts both tablets together and waits for them.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while joining
     */
    public static void main(String[] args) throws InterruptedException {
       
    	Thread t1 = new Thread(() -> runTablet(new String[] 
        		{"Kopi O x2", "Roti bakar x1", "Teh tarik x1"}), "Tablet 1");
    	
        Thread t2 = new Thread(() -> runTablet(new String[] {
        		"Mee goreng x1", "Nasi lemak ayam x2", "Milo ais x1"}), "Tablet 2");
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * Sends a list of orders on its own connection and reports the finishing time.
     *
     * @param orders the orders this tablet sends
     */
    private static void runTablet(String[] orders) {
        
    	String me = Thread.currentThread().getName();
        
    	try (Socket socket = new Socket("localhost", 5050);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), 
                    		 StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
            		 socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            for (String order : orders) {
                out.println(order);
                
                // wait for the kitchen's confirmation
                in.readLine();                    
            }
            
            out.println("BYE");
            in.readLine();
            System.out.printf("[%s] all 3 orders confirmed after %.1f s%n", 
            		me, (System.nanoTime() - START) / 1e9);
            
        } catch (IOException e) {
        	
            System.err.println("[" + me + "] cannot reach the kitchen: " + e.getMessage());
        }
    }
}