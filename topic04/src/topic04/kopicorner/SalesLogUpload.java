package topic04.kopicorner;

/*
 * Program 4.4: SalesLogUpload.java
 *
 * Program description:
 *   At closing time, a background thread on Kopi Corner's counter tablet 1
 *   uploads the day's sales log to the kitchen server, one line every 0.3
 *   seconds. Kak Ros switches the tablet off after 1.2 seconds, so the main
 *   thread interrupts the upload, then waits for it to stop cleanly before
 *   the tablet shuts down.
 *
 * What it shows:
 *   interrupt() as a polite request to stop, how a thread notices it (an
 *   InterruptedException from sleep(), or isInterrupted()), and join() with
 *   a time limit.
 *
 * How to run:
 *   Run as a Java application.
 */
public class SalesLogUpload {

	/**
	 * Starts the upload, interrupts it at switch-off time, and waits for it.
	 *
	 * @param args not used
	 * @throws InterruptedException if the main thread is interrupted
	 */
	public static void main(String[] args) throws InterruptedException {

		Thread uploader = new Thread(SalesLogUpload::upload, "uploader");
		uploader.start();

		Thread.sleep(1200); 
		// Kak Ros presses the power button
		System.out.println("[main]     Switching off: interrupting the upload");
		// a request, not a command: the uploader decides how to stop
		uploader.interrupt();                    

		// wait at most 2 seconds for it to finish
		uploader.join(2000);                     
		System.out.println("[main]     Uploader alive? " + uploader.isAlive() 
		+ ". Tablet off.");
	}

	/**
	 * Uploads up to ten sales log lines, stopping early if interrupted.
	 */
	private static void upload() {

		int sent = 0;
		try {

			for (int line = 1; line <= 10; line++) {

				// check between pieces of work
				if (Thread.currentThread().isInterrupted()) {  
					break;
				}
				Thread.sleep(300);
				// sending one line over the café network
				sent++;
				System.out.println("[uploader] Sent sales log line " + line + " of 10");
			}

		} catch (InterruptedException e) {

			// sleep() throws this when interrupt() is called while the thread is sleeping
			System.out.println("[uploader] Interrupted while sending: stopping");

		} finally {

			// Always leave a clear record, even after an interrupt
			System.out.println("[uploader] " + sent + " lines sent; the rest will "
					+ "go tomorrow morning");
		}
	}
}
