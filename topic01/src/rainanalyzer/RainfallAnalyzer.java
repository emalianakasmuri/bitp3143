package rainanalyzer;

/**
 * The class provides the three rainfall tasks as follows:-
 *   - Task A, stationTotal(): independent work that could run in parallel.
 *   - Task B, wettestStation(): a reduction that needs all of Task A's results.
 *   - Task C, cumulative(): a loop-carried dependency that must run in order.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class RainfallAnalyzer {

    /**
     * This method represents Task A where it gets a total rainfall for 
     * ONE station.  It reads only that station's row and writes nothing shared,
     * which is why different stations can be processed at the same time.
     *
     * @param station the station's index
     * @return total rainfall in mm
     */
    public static double getStationTotal(int station) {
    	
        double total = 0;
        
        for (double mm : RainfallData.RAIN[station]) 
        	total += mm;
        
        return total;
    }

    /**
     * This method represents Task B, where it finds the wettest station. 
     * Needs ALL results of Task A (a reduction).
     *
     * @param totals the total rainfall of every station
     * @return the index of the station with the highest total
     */
    public static int getWettestStation(double[] totals) {
        
    	int wettest = 0;
        
    	for (int index = 1; index < totals.length; index++) {
            
    		if (totals[index] > totals[wettest]) 
            	wettest = index;   
        }
    	
        return wettest;
    }

    /**
     * This method represents Task C: The task running (cumulative) rainfall at 
     * one station.
     * 
     * Loop-carried dependency: hour h needs the result of hour h-1.
     *
     * @param station the station's index
     * @return cumulative rainfall after each hour, in mm
     */
    public static double[] getCumulative(int station) {
    	
        double[] result = new double[RainfallData.RAIN[station].length];
        
        result[0] = RainfallData.RAIN[station][0];
        for (int index = 1; index < result.length; index++) {
        	
            result[index] = result[index - 1] + RainfallData.RAIN[station][index];   
        }
        
        return result;
    }

    // Utility class: no objects are created
    private RainfallAnalyzer() {
    	
    }
}