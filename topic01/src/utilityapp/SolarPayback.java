package utilityapp;

/**
 * This program models three tasks for a home rooftop solar system: working out 
 * how many years the bill savings take to cover the installation cost, 
 * connecting to the solar inverter with retries, and averaging daily energy 
 * readings while ignoring faulty ones.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */

public class SolarPayback {
	
	/**
     * This method counts the years until cumulative savings reach the system cost.
     * Savings grow as tariffs rise and shrink as the panels age.
     *
     * @return the year in which payback is reached
     */
    static int paybackYear(double systemCost, double firstYearSaving,
                           double tariffGrowth, double panelDecline) {
        
    	
    	double yearlySaving = firstYearSaving;
        double cumulative = 0.0;     
        int year = 0;

        // while: we do not know in advance how many years it will take
        while (cumulative < systemCost) {
            
        	year++;
            cumulative += yearlySaving;
            System.out.printf("Year %2d: saved RM%8.2f, cumulative RM%9.2f%n",
                    year, yearlySaving, cumulative);
            
            // Next year's saving: bills rise, panel output falls slightly
            yearlySaving *= (1 + tariffGrowth) * (1 - panelDecline);
        }
        
        return year;
    }

    /**
     * This method simulates connecting to the solar inverter, retrying on failure.
     * For this demonstration the connection succeeds on the third attempt.
     *
     * @param maxAttempts the most attempts allowed before giving up
     */
    static void connectToInverter(int maxAttempts) {
        int attempt = 0;
        boolean connected;

        // do-while: the body always runs at least once
        do {
        
        	attempt++;
        	
        	// simulate: succeeds on 3rd try
            connected = (attempt == 3);   
            
            System.out.println("Connecting to inverter, attempt " + attempt
                    + (connected ? ": OK" : ": failed"));
        
        } while (!connected && attempt < maxAttempts);   // two exit conditions
    }

    /**
     * This method averages daily energy readings, skipping faulty values 
     * (zero or below).
     */
    static void averageValidReadings(double[] dailyKwh) {
        
    	double sum = 0;
        int valid = 0;

        for (double kwh : dailyKwh) {
        	
            if (kwh <= 0) {
            	// skip faulty or zero readings
                continue;               
            }
            
            // only valid readings reach here
            sum += kwh;                 
            valid++;
        }
        
        System.out.printf("\nAverage of %d valid days: %.2f kWh%n", 
        		valid, sum / valid);
    }

    /**
     * Main entry point of the program 
     * 
     * It runs the three solar tasks with sample figures.
     */
    public static void main(String[] args) {
    	
    	System.out.println("A demonstration of solar payback\n");
        
    	// RM18,000 system, RM3,000 saved in year 1, bills +3%/year, 
    	// panels -0.5%/year
        int year = paybackYear(18000.00, 3000.00, 0.03, 0.005);
        System.out.println("Payback reached in year " + year + "\n");

        connectToInverter(5);

        averageValidReadings(new double[] {18.2, -1.0, 21.5, 0.0, 19.8});
        
        System.out.println("\nProgram ends");
    }

}
