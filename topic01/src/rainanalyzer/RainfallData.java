package rainanalyzer;

/**
 * This class provides rainfall data.  It holds six hours of rainfall readings 
 * from four rain gauges. The data is read-only, so any number of tasks can 
 * read it safely at once.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class RainfallData {

    // Station names; index here matches row in RAIN. 
    public static final String[] STATIONS = {"Kuantan", "Kota Bharu", 
    		"Kuala Terengganu", "Segamat"};

    // One row per station, one column per hour (rainfall in mm)
    public static final double[][] RAIN = {
        {12.0, 30.5, 44.0, 18.5,  6.0,  2.5},
        {25.0, 41.0, 52.5, 38.0, 20.0, 11.0},
        { 8.5, 15.0, 22.0, 35.5, 29.0, 14.0},
        { 3.0,  9.5, 27.0, 48.5, 33.0, 12.5}
    };

    // Utility class: no objects are created
    private RainfallData() { }
}