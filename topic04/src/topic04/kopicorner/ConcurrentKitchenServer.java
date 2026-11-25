package topic04.kopicorner;

/*
 * Program 4.5: ConcurrentKitchenServer.java
 *
 * Program description:
 *   The Kopi Corner kitchen server from Topic 3, now able to serve both
 *   counter tablets at the same time. Each accepted tablet is handed to its
 *   own thread, so the main thread can go straight back to accept().
 *   Printing each kitchen ticket takes 0.5 seconds.
 *
 * What it shows:
 *   The thread-per-client pattern: one listening thread plus one worker
 *   thread for every connected client. Set CONCURRENT to false to get the
 *   one-tablet-at-a-time behaviour of Topic 3 for comparison.
 *
 * How to run:
 *   Start this program first, then run LunchRush (Program 4.6).
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConcurrentKitchenServer {

	/** Port number on which the kitchen server listens. */
	private static final int PORT = 5050;

	/** true: one thread per tablet. false: one tablet at a time, as in Topic 3. */
	private static final boolean CONCURRENT = true;

	/** Time taken to print one kitchen ticket, in milliseconds. */
	private static final long TICKET_MS = 500;

	/**
	 * Accepts tablets for ever, giving each one its own thread.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {


		try (ServerSocket serverSocket = new ServerSocket(PORT)) {

			System.out.println("[main] Kitchen listening on port " + PORT
					+ (CONCURRENT ? " (one thread per tablet)" : " (one tablet at a time)"));

			int tabletNo = 0;

			while (true) {

				// only the main thread calls accept()
				Socket tablet = serverSocket.accept();     
				tabletNo++;

				if (CONCURRENT) {

					Thread worker = new Thread(
							() -> handleTablet(tablet), "tablet-" + tabletNo);

					// main returns to accept() at once
					worker.start();  

				} else {

					// main is busy until this tablet leaves
					handleTablet(tablet);                   
				}
			}

		} catch (IOException e) {

			System.err.println("[main] Could not listen on port " + PORT + ": " 
					+ e.getMessage());
		}
	}

	/**
	 * Serves one tablet until it sends BYE. Runs on that tablet's own thread.
	 * Every variable here is local, so each thread has its own copy.
	 *
	 * @param tablet the socket returned by accept()
	 */
	private static void handleTablet(Socket tablet) {

		String me = Thread.currentThread().getName();
		System.out.println("[" + me + "] connected");

		try (tablet;
				BufferedReader in = new BufferedReader(
						new InputStreamReader(tablet.getInputStream(), 
								StandardCharsets.UTF_8));
				PrintWriter out = new PrintWriter(tablet.getOutputStream(), 
						true, StandardCharsets.UTF_8)) {

			String line;
			int count = 0;

			// local: belongs to this tablet only
			while ((line = in.readLine()) != null) {

				if (line.equalsIgnoreCase("BYE")) {
					
					out.println("GOODBYE");
					break;
				}

				Thread.sleep(TICKET_MS);                     
				count++;

				// printing the kitchen ticket
				System.out.println("[" + me + "] ticket " + count + ": " + line);
				out.println("RECEIVED " + line.toUpperCase());
			}

		} catch (IOException e) {

			System.err.println("[" + me + "] problem: " + e.getMessage());

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
		}

		System.out.println("[" + me + "] disconnected");
	}
}