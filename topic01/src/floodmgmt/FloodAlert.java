package floodmgmt;

/**
 * This class demonstrate flood alert decision using Boolean logic.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development 
 */
public class FloodAlert {

    // Thresholds (metres) for one river station.
    // "static final" makes them constants: shared by the class and never 
	// changed.
    static final double ALERT_LEVEL   = 3.0;   
    static final double WARNING_LEVEL = 4.0;
    static final double DANGER_LEVEL  = 5.0;

    /**
     * This method decides whether a station should order an evacuation.
     * 
     * @return true or false, 
     */
    static boolean shouldEvacuate(double waterLevel, boolean heavyRain, 
    		boolean highTide) {
        
    	// Relational operators (>=) turn raw readings into true/false 
    	// conditions
        boolean aboveDanger  = waterLevel >= DANGER_LEVEL;
        boolean aboveWarning = waterLevel >= WARNING_LEVEL;

        // Logical operators combine the conditions into one decision:
        // evacuate if above danger level, OR above warning level AND
        // (heavy rain OR high tide) is making things worse.
        // The parentheses make the intended grouping explicit.
        return aboveDanger || (aboveWarning && (heavyRain || highTide));
    }

    /**
     * Main entry point of the program
     * 
     * @param args
     */
    public static void main(String[] args) {
        
    	// Test data: one entry per station, kept in parallel arrays.
        // Index i in every array describes the same station.
        String[] stations = {"Station A", "Station B", "Station C", "Station D"};
        
        // water level in metres
        double[] levels   = {5.2, 4.3, 4.1, 3.4};
        
        // is heavy rain falling?
        boolean[] rain    = {false, true, false, true};  
        
        // is it high tide?
        boolean[] tide    = {false, false, false, true};    

        // Table heading
        System.out.println("Station     Level  Rain   Tide   Evacuate?");

        // Check each station in turn and print one row per station
        for (int index = 0; index < stations.length; index++) {
            
        	// Apply the Boolean rule to this station's readings
            boolean evacuate = shouldEvacuate(levels[index], rain[index], 
            		tide[index]);
            
            // %-11s = left-aligned text, %4.1f = number with 1 decimal place, 
            // %b = boolean
            System.out.printf("%-11s %4.1fm  %-6b %-6b %b%n",
                    stations[index], levels[index], rain[index], tide[index], 
                    evacuate);
        }
    }
}