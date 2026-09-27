package campusmgmt;

/**
 * This class coordinates low cohesion implementation
 * 
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development
 */
public class CampusManagerDemo {
	
	/**
	 * Main entry point of the application
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Invoking unrelated operation
        CampusManager campusManager = new CampusManager();
        campusManager.addMark(78); 
        campusManager.addMark(64); 
        campusManager.addMark(91);
        
        campusManager.recordOffence(); 
        campusManager.recordOffence();
        
        campusManager.recordSale(8.50); 
        campusManager.recordSale(12.00);

        // Displaying output
        System.out.printf("Average mark : %.2f%n", campusManager.averageMark());
        System.out.printf("Parking fines: RM%.2f%n", campusManager.totalFines());
        System.out.printf("Cafe sales   : RM%.2f%n", campusManager.salesToday());
    }


}
