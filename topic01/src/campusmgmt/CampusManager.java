package campusmgmt;

/**
 * This class demonstrate LOW cohesion (an example of what to avoid), where 
 * three unrelated jobs share one class, and each job uses its own fields.
 * 
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development
 * 
 */
public class CampusManager {

	// Fields used only by the grade methods
	private int totalMarks = 0;
	private int subjects = 0;

	// Fields used only by the parking methods
	private int parkingOffences = 0;
	// RM
	private static final double FINE_PER_OFFENCE = 30.00;   

	// Fields used only by the cafeteria methods
	private double cafeteriaSales = 0.0;

	// --- Job 1: academic results ---
	public void addMark(int mark) { 
	
		totalMarks += mark; subjects++; 
	}
	
	public double averageMark() {
		
		return subjects == 0 ? 0 : (double) totalMarks / subjects; 
	}

	// --- Job 2: parking enforcement ---
	public void recordOffence() { 
		
		parkingOffences++; 
	}
	
	public double totalFines() { 
		
		return parkingOffences * FINE_PER_OFFENCE; 
	}

	// --- Job 3: cafeteria sales ---
	public void recordSale(double amount) { 
		
		cafeteriaSales += amount;
	}
	
	public double salesToday() { 
		return cafeteriaSales; 
	}
}
