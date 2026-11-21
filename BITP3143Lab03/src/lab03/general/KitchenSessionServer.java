package lab03.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * Exercise 7 : queues many orders from one tablet until BYE. 
 * 
 */
public class KitchenSessionServer {

	// Server program port no
	static final int PORT = 5050;

	/**
	 * Main entry point to the application
	 * @param args
	 */
	public static void main(String[] args) {

		// Keeping track next queue number
		int nextQueueNo = 101;

		try (ServerSocket server = new ServerSocket(PORT)) {

			System.out.println("Kitchen server waiting on port " + PORT + "...");

			try (Socket tablet = server.accept();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(tablet.getInputStream()));
					PrintWriter out = new PrintWriter(tablet.getOutputStream(), true)) {

				System.out.println("Tablet connected.");
				String line;
				
				// null = tablet closed the connection
				while ((line = in.readLine()) != null) {      
					
					// TODO 1: if line is BYE (ignore case), 
					//         reply "BYE from kitchen" and break.
					
					// TODO 2: split the line on commas. 
					//         If there are not 3 parts,
					//		   reply "REJECTED expected tablet,item,quantity" 
					//         and continue.
					
					// TODO 3: call OrderLogger.validateQuantity(parts[2]) in try-catch.
					//         Valid: reply "QUEUED #" + nextQueueNo + " " + line, 
					//         print it, add 1 to nextQueueNo.
					//         Invalid: reply "REJECTED " + the exception's message.
				}
			}
			
			System.out.println("Session ended. " + (nextQueueNo - 101) 
				+ " order(s) queued.");
			
		} catch (IOException e) {
			
			System.out.println("Kitchen server error: " + e.getMessage());
			
		}
	}
}