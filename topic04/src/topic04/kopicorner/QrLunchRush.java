package topic04.kopicorner;

/*
 * Program 4.8: QrLunchRush.java
 *
 * Program description:
 *   Simulates 200 customers at Kopi Corner sending a QR order from their
 *   phones at the same time. Each phone prints one message with the
 *   server's reply as it arrives (200 messages for 200 phones). Then it
 *   reports the total time, how many orders were queued and how many
 *   different server worker threads did the work.
 *
 * What it shows:
 *   Using a thread pool on the client side to create load, collecting the
 *   replies safely in a ConcurrentHashMap, and shutdown() with
 *   awaitTermination().
 *
 * How to run:
 *   Start QrOrderServer (Program 4.7) first, then run this program.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class QrLunchRush {

    /** Number of phones in the simulation. */
    private static final int PHONES = 200;

    /** Items the simulated customers order, in turn. */
    private static final String[] ITEMS = {"Kopi O x1", "Teh tarik x1", "Mee goreng x1", 
    	"Roti bakar x2"};

    /**
     * Fires all orders at once and summarises the replies.
     *
     * @param args not used
     * @throws InterruptedException if interrupted while waiting for the pool
     */
    public static void main(String[] args) throws InterruptedException {
        
    	// Phone ID -> reply. Many threads write here, so the map must be a concurrent one.
        Map<String, String> replies = new ConcurrentHashMap<>();
        ExecutorService phones = Executors.newFixedThreadPool(PHONES);

        long start = System.nanoTime();
        for (int i = 1; i <= PHONES; i++) {
        	
            String phoneId = String.format("phone-%03d", i);
            String item = ITEMS[i % ITEMS.length];
            phones.execute(() -> {
            	
                String reply = order(item);
                replies.put(phoneId, reply);
                
                // one message per phone
                System.out.println("[" + phoneId + "] " + reply);   
                
            });
        }
        
     // no more tasks will be submitted
        phones.shutdown();      
        
     // wait for all submitted tasks to finish
        phones.awaitTermination(1, TimeUnit.MINUTES);          
        double seconds = (System.nanoTime() - start) / 1e9;

        long queued = replies.values().stream().filter(r -> r.startsWith("200")).count();
        long workers = replies.values().stream().filter(r -> r.startsWith("200"))
                .map(r -> r.substring(r.lastIndexOf(' ') + 1)).distinct().count();
        System.out.printf("%d of %d orders queued in %.1f s by %d server worker threads%n",
                queued, PHONES, seconds, workers);
    }

    /**
     * Sends one order and returns the server's reply.
     *
     * @param item the item ordered
     * @return the reply line, or an error description
     */
    private static String order(String item) {
    	
        try (Socket socket = new Socket("localhost", 5100);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), 
                    		 StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, 
            		 StandardCharsets.UTF_8)) {
        	
            out.println("ORDER " + item);
            return in.readLine();
            
        } catch (IOException e) {
        	
            return "ERROR " + e.getMessage();
        }
    }
}