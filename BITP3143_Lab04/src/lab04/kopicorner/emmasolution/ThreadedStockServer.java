/*
 * Lab 04, Exercise 7 (preview of Topic 4): ThreadedStockServer.java
 *
 * Program description:
 *   StockServer with one change: each accepted client is served on its own
 *   thread, so the main thread goes straight back to accept(). The kitchen
 *   screen and counter tablet T1 can now be served at the same time.
 *
 * What it shows:
 *   A first look at the thread-per-client design that Topic 4 explains in
 *   full. All client threads share one StockProtocol object. That is enough
 *   for this preview, but two threads updating the egg count at the same
 *   instant could leave it wrong: Topic 5 shows how to prevent that.
 *
 * How to run:
 *   Stop StockServer first (both use port 6100). Start this program, then
 *   run KitchenScreen and StockTablet.
 */
package lab04.kopicorner.emmasolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ThreadedStockServer {

    /** Port number on which the store-room server listens. */
    private static final int PORT = 6100;

    /** One protocol object shared by every client thread. */
    private static final StockProtocol PROTOCOL = new StockProtocol();

    /**
     * Accepts clients for ever, giving each one its own thread.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        
    	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            
    		System.out.println("[main] KOPI-STOCK/1.0 ready on port " 
    				+ PORT + " (one thread per client)");
            int clientNo = 0;
            
            while (true) {
            	
            	// only the main thread calls accept()
                Socket client = serverSocket.accept();           
                clientNo++;
                
                // main returns to accept() at once
                Thread worker = new Thread(() -> serve(client), "client-" + clientNo);
                worker.start();                                  
            }
        
    	} catch (IOException e) {
        	
            System.err.println("[main] Cannot listen on port " 
            	+ PORT + ": " + e.getMessage());
        }
    }

    /**
     * Serves one client until it sends QUIT. Runs on that client's own thread.
     *
     * @param client the socket returned by accept()
     */
    private static void serve(Socket client) {
        
    	String me = Thread.currentThread().getName();
        System.out.println("[" + me + "] connected");
        
        // try with resources
        try (client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), 
                    		 StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true, 
            		 StandardCharsets.UTF_8)) {

            out.println("220 KOPI-STOCK/1.0 store ready");
            
            String request;
            while ((request = in.readLine()) != null) {
            	
                String reply = PROTOCOL.handle(request);
                System.out.println("[" + me + "] " + request + "  =>  " 
                		+ reply.replace("\n", " | "));
                out.println(reply);
                
                if (StockProtocol.isFinal(reply)) {
                    break;
                }
            }
            
        } catch (IOException e) {
        	
            System.err.println("[" + me + "] problem: " + e.getMessage());
        }
        System.out.println("[" + me + "] disconnected");
    }
}
