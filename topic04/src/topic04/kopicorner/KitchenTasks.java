package topic04.kopicorner;

/*
 * Program 4.1: KitchenTasks.java
 *
 * Program description:
 *   The single Kopi Corner cook prepares one order with three jobs: boil the
 *   noodles, fry an egg and pour a drink. Each job is mostly waiting (water
 *   heating, egg setting). The program times the jobs done one after another,
 *   then done concurrently on three threads.
 *
 * What it shows:
 *   Creating threads from lambdas, start(), join(), and why overlapping the
 *   waiting time makes the whole order finish sooner.
 *
 * How to run:
 *   Run as a Java application. No other program is needed.
 */
public class KitchenTasks {

	/** Time at which the current measurement started. */
	private static long start;

	/**
	 * Runs the three jobs sequentially, then concurrently, and prints both times.
	 *
	 * @param args not used
	 * @throws InterruptedException if the main thread is interrupted while joining
	 */
	public static void main(String[] args) throws InterruptedException {

		System.out.println("--- One job after another ---");
		start = System.nanoTime();
		boilNoodles();
		fryEgg();
		pourDrink();
		System.out.printf("Order ready after %.1f s%n%n", elapsed());

		System.out.println("--- Three jobs on three threads ---");
		start = System.nanoTime();

		// a Runnable written as a lambda
		Thread noodles = new Thread(() -> boilNoodles(), "noodles");   
		Thread egg = new Thread(KitchenTasks::fryEgg, "egg");  
		// a method reference works too
		Thread drink = new Thread(KitchenTasks::pourDrink, "drink");

		// start() asks the JVM to run the job on a new thread
		noodles.start();     
		egg.start();
		drink.start();
		
		// join() waits until that thread has finished
		noodles.join();      
		egg.join();
		drink.join();
		System.out.printf("Order ready after %.1f s%n", elapsed());
	}

	/** 
	 * Boils noodles: 1.5 seconds, mostly waiting for the water. 
	 */
	private static void boilNoodles() {
		job("boil noodles", 1500);
	}

	/** 
	 * Fries an egg: 1.0 second. 
	 */
	private static void fryEgg() {
		job("fry egg", 1000);
	}

	/** 
	 * Pours a drink: 0.5 seconds. 
	 */
	private static void pourDrink() {
		job("pour Kopi O", 500);
	}

	/**
	 * Simulates one kitchen job that takes some time.
	 *
	 * @param name   what the job is
	 * @param millis how long the job takes, in milliseconds
	 */
	private static void job(String name, long millis) {
		
		String who = Thread.currentThread().getName();
		System.out.printf("[%4.1fs] %-6s starts  %s%n", elapsed(), who, name);
		
		try {
			
			// stands in for waiting on the stove
			Thread.sleep(millis);    
			
		} catch (InterruptedException e) {
			// keep the interrupt flag for the caller
			Thread.currentThread().interrupt();   
			return;
		}
		
		System.out.printf("[%4.1fs] %-6s done    %s%n", elapsed(), who, name);
	}

	/**
	 * Seconds since the current measurement started.
	 *
	 * @return elapsed time in seconds
	 */
	private static double elapsed() {
		
		return (System.nanoTime() - start) / 1e9;
	}
}