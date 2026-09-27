package rainanalyzer;

/**
 * Program 1.12a: RainfallData.java
 *
 * Class description (data):
 *   Holds six hours of rainfall readings from four hypothetical rain gauges.
 *   The data is read-only, so any number of tasks can read it safely at once.
 */
public class RainfallData {

    /** Station names; index s here matches row s in RAIN. */
    public static final String[] STATIONS = {"Kuantan", "Kota Bharu", 
    		"Kuala Terengganu", "Segamat"};

    /** One row per station, one column per hour (rainfall in mm). */
    public static final double[][] RAIN = {
        {12.0, 30.5, 44.0, 18.5,  6.0,  2.5},
        {25.0, 41.0, 52.5, 38.0, 20.0, 11.0},
        { 8.5, 15.0, 22.0, 35.5, 29.0, 14.0},
        { 3.0,  9.5, 27.0, 48.5, 33.0, 12.5}
    };

    // Utility class: no objects are created
    private RainfallData() { }
}