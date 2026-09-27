package rainanalyzer;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * This program analyses six hours of rainfall from four  rain gauges. It 
 * totals the rain at each station, finds the wettest station, and builds a 
 * running (cumulative) total for that station.
 *
 * The program demonstrate the following:-
 *   - Task A (station totals): independent tasks that could run in parallel.
 *   - Task B (wettest station): a reduction that needs all of Task A's results.
 *   - Task C (cumulative rainfall): a loop-carried dependency that must run in 
 *   order.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class IndependentTasks {

    /**
     * Runs Tasks A, B and C, then checks that a parallel Task A matches.
     */
    public static void main(String[] args) {
        String[] stations = RainfallData.STATIONS;

        // Task A for every station: independent -> could run in parallel
        double[] totals = new double[stations.length];
        
        for (int index = 0; index < stations.length; index++) {
        	
        	// each iteration writes its own slot
            totals[index] = RainfallAnalyzer.getStationTotal(index);   
            System.out.printf("%-17s total %6.1f mm%n", stations[index], 
            		totals[index]);
        }

        // Task B: a reduction over all of Task A's results
        int wettest = RainfallAnalyzer.getWettestStation(totals);
        System.out.println("Wettest station: " + stations[wettest]);

        // Task C: must run hour by hour, in order
        double[] cumulative = RainfallAnalyzer.getCumulative(wettest);
        System.out.println("Cumulative at " + stations[wettest] + ": "
                + Arrays.toString(cumulative));

        // Task A run in parallel gives the same answer.
        // parallel() lets Java share the stations among the available cores.
        double[] parallelTotals = IntStream.range(0, stations.length)
        		.parallel()
        		.mapToDouble(RainfallAnalyzer::getStationTotal)
        		.toArray();
        
        System.out.println("Parallel Task A matches sequential? "
                + Arrays.equals(totals, parallelTotals));
    }
}