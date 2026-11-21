package lab03.kopicorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

/** 
 * Exercise 6 (client): counter tablet T1 sends one order to the kitchen. 
 */
public class CounterTablet {

	// Variable for server connection
	static final String HOST = "localhost";
	static final int PORT = 5050;

	/**
	 * Main entry point to the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// try with resources
		try (Socket socket = new Socket(HOST, PORT);

			// Establishing stream objects to communicate with the server
			// program
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()))) {

			// Order data - tablet, item code, quantity
			String order = "T1,D02,2";                  
			System.out.println("Sending order: " + order);
			
			// Sending order data to the kitchen server
			out.println(order);
			
			// Reading replies from the kitchen server
			String reply = in.readLine();
			System.out.println("Kitchen replied: " + reply);
			
			

		} catch (ConnectException e) {

			System.out.println("Cannot reach the kitchen server. "
					+ "Is it running on port " + PORT + "?");

		} catch (IOException e) {

			System.out.println("Network problem: " + e.getMessage());
		}
		
		System.out.println("Program ends");
	}
}