package topic04.kopicorner;

/*
 * Program 4.7: QrOrderServer.java
 *
 * Program description:
 *   The Kopi Corner QR order server. Customers scan the QR code on their
 *   table and their phone sends one line, "ORDER <item>". Queuing an order
 *   for the kitchen takes 0.2 seconds. Connections are handled by a fixed
 *   pool of 20 worker threads, however many phones arrive.
 *
 * What it shows:
 *   ExecutorService and Executors.newFixedThreadPool(): the main thread only
 *   accepts connections and submits them as tasks; the pool's workers do
 *   the rest. Extra tasks wait in the pool's queue.
 *
 * How to run:
 *   Start this program first, then run QrLunchRush (Program 4.8).
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrOrderServer {

    /** Port number on which the QR order server listens. */
    private static final int PORT = 5100;

    /** Number of worker threads in the pool. */
    private static final int POOL_SIZE = 20;

    /** Time taken to queue one order for the kitchen, in milliseconds. */
    private static final long QUEUE_MS = 200;

    /**
     * Accepts phone connections and hands each one to the pool.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        
    	ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        
    	// backlog of 200 waiting connections
    	try (ServerSocket serverSocket = new ServerSocket(PORT, 200)) {   
            
    		System.out.println("[QR] Listening on port " + PORT + " with " 
    				+ POOL_SIZE + " worker threads");
            
    		while (true) {
    			
                Socket phone = serverSocket.accept();
                
                // a task, not a new thread
                pool.execute(() -> handlePhone(phone));       
            }
    		
        } catch (IOException e) {
        	
            System.err.println("[QR] Could not listen on port " 
            		+ PORT + ": " + e.getMessage());
        
        } finally {
            
        	// stop accepting tasks; let running ones finish
        	pool.shutdown();                                   
        }
    }

    /**
     * Queues one order and replies. Runs on a pool worker thread.
     *
     * @param phone the connected phone
     */
    private static void handlePhone(Socket phone) {
    	
        try (phone;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(phone.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(phone.getOutputStream(), true, 
            		 StandardCharsets.UTF_8)) {

            String request = in.readLine();
            
            if (request == null || !request.startsWith("ORDER ")) {
            	
                out.println("400 BAD_REQUEST");
                return;
            }
            Thread.sleep(QUEUE_MS); 
            
            // passing the order to the kitchen queue
            out.println("200 QUEUED " + request.substring(6) + " by " 
            		+ Thread.currentThread().getName());
            
        } catch (IOException e) {
        	
            System.err.println("[QR] phone problem: " + e.getMessage());
            
        } catch (InterruptedException e) {
        	
            Thread.currentThread().interrupt();
        }
    }
}