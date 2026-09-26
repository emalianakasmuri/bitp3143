package floodmgmt;

/**
 * Program 1.2: Flood alert decision using Boolean logic.
 * Water-level thresholds are illustrative, modelled on the
 * Normal / Alert / Warning / Danger levels used on Malaysian river gauges.
 */
public class FloodAlert {

    // Illustrative thresholds (metres) for one hypothetical river station.
    // "static final" makes them constants: shared by the class and never changed.
    static final double ALERT_LEVEL   = 3.0;   // not used in this rule; kept to show all levels
    static final double WARNING_LEVEL = 4.0;
    static final double DANGER_LEVEL  = 5.0;

    /**
     * Decides whether a station should order an evacuation.
     * Returns true or false, so the method itself is a Boolean expression.
     */
    static boolean shouldEvacuate(double waterLevel, boolean heavyRain, boolean highTide) {
        
    	// Relational operators (>=) turn raw readings into true/false conditions
        boolean aboveDanger  = waterLevel >= DANGER_LEVEL;
        boolean aboveWarning = waterLevel >= WARNING_LEVEL;

        // Logical operators combine the conditions into one decision:
        // evacuate if above danger level, OR above warning level AND
        // (heavy rain OR high tide) is making things worse.
        // The parentheses make the intended grouping explicit.
        return aboveDanger || (aboveWarning && (heavyRain || highTide));
    }

    public static void main(String[] args) {
        
    	// Test data: one entry per station, kept in parallel arrays.
        // Index i in every array describes the same station.
        String[] stations = {"Station A", "Station B", "Station C", "Station D"};
        double[] levels   = {5.2, 4.3, 4.1, 3.4};           // water level in metres
        boolean[] rain    = {false, true, false, true};     // is heavy rain falling?
        boolean[] tide    = {false, false, false, true};    // is it high tide?

        // Table heading
        System.out.println("Station     Level  Rain   Tide   Evacuate?");

        // Check each station in turn and print one row per station
        for (int i = 0; i < stations.length; i++) {
            
        	// Apply the Boolean rule to this station's readings
            boolean evacuate = shouldEvacuate(levels[i], rain[i], tide[i]);
            // %-11s = left-aligned text, %4.1f = number with 1 decimal place, %b = boolean
            System.out.printf("%-11s %4.1fm  %-6b %-6b %b%n",
                    stations[i], levels[i], rain[i], tide[i], evacuate);
        }
    }
}