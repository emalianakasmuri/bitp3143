package lab03.kopicorner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * Exercise (server): the kitchen server accepts one counter tablet 
 * and one order. 
 */
public class KitchenServer {

	// Port for kitchen server program
	static final int PORT = 5050;

	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// try-with resources block
		try (ServerSocket server = new ServerSocket(PORT)) {

			// Initial message indicating the server program is running
			// at the correct port
			System.out.println("Kitchen server waiting on port " + PORT + "...");

			// accept() blocks (waits) until a tablet connects
			try (Socket tablet = server.accept();

				// Establish input and output stream to receive and send message
				// back to the client
				BufferedReader in = new BufferedReader(
					new InputStreamReader(tablet.getInputStream()));
				PrintWriter out = new PrintWriter(tablet.getOutputStream(), true)) {

					System.out.println("Tablet connected from " 
						+ tablet.getInetAddress().getHostAddress());
					
					// blocks until a full line arrives from the client
					String order = in.readLine();            				
					System.out.println("Order received: " + order);
					
					// Responding to the client
					out.println("RECEIVED " + order);  
				
			}
			
			System.out.println("Connection closed. Kitchen server stopping.");
			
		} catch (IOException e) {
			
			System.out.println("Kitchen server error: " + e.getMessage());
		}
	}
}
