package topic04.kopicorner;

/*
 * Program 4.2 (part 4 of 4): ThreeWaysToThread.java
 *
 * Program description:
 *   Three background jobs at Kopi Corner, each written in a different way:
 *   a number caller that extends Thread (NumberCaller), a stock reminder
 *   that implements Runnable (StockReminder), and a screen refresher written
 *   as a lambda.
 *
 * What it shows:
 *   The three common ways to define the work of a thread, thread names,
 *   and that start() runs the work on a new thread while run() does not.
 *
 * How to run:
 *   Put KitchenLog, NumberCaller, StockReminder and this class in the same
 *   project, then run this class as a Java application. The order of the
 *   lines may differ slightly from run to run; that is normal for
 *   concurrent programs.
 */
public class ThreeWaysToThread {

	/**
	 * Starts the three jobs and waits for them all.
	 *
	 * @param args not used
	 * @throws InterruptedException if interrupted while joining
	 */
	public static void main(String[] args) throws InterruptedException {

		// Way 1 - task to call order number - 
		//NumberCaller extends Thread
		Thread caller = new NumberCaller();                              

		// Way 2 - check stock
		// Creating object form StockReminder implements Runnable
		Thread reminder = new Thread(new StockReminder(), "reminder");   

		// Way 3: a lambda - print message for background task
		Thread screen = new Thread(() -> {                               
			for (int i = 1; i <= 3; i++) {
				KitchenLog.say("Kitchen screen refreshed");
				KitchenLog.pause(400);
			}
		}, "screen");

		// start() asks the JVM to run the job on a new thread
		KitchenLog.say("Starting three background jobs");
		caller.start();  // indirectly invoking run()
		reminder.start();
		screen.start();

		// join() waits until that thread has finished
		caller.join();
		reminder.join();
		screen.join();
		KitchenLog.say("All jobs finished");

		System.out.println();
		KitchenLog.say("Calling run() directly instead of start():");
		// no new thread: the work runs on main
		new NumberCaller().run();   
	}
}