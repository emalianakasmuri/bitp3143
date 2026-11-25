package program23;

/*
 * Program 2.3 (file 1 of 2): ApiReading.java
 *
 * Program description:
 *   An immutable record for one hourly Air Pollutant Index (API) reading
 *   from an air-quality monitoring station.
 *
 * What it shows:
 *   Converting one line of a CSV file into a typed Java object, and keeping
 *   the parsing rule in one place.
 *
 * Class description:
 *   ApiReading is a Java record (Java 16+) with a factory method that parses
 *   a CSV line and a method that returns the Malaysian API status category.
 */
public record ApiReading(String station, String state, String hour, int api) {

    /**
     * Builds a reading from one CSV line in the form
     * station,state,hour,api  e.g. "Bukit Rambai,Melaka,08:00,48".
     *
     * @param line one data line from the CSV file
     * @return the parsed reading
     */
    public static ApiReading fromCsv(String line) {
        String[] parts = line.split(",");
        return new ApiReading(parts[0].trim(), parts[1].trim(),
                parts[2].trim(), Integer.parseInt(parts[3].trim()));
    }

    /**
     * Maps an API value to the status band used in Malaysia.
     *
     * @param value the API value
     * @return Good, Moderate, Unhealthy, Very Unhealthy or Hazardous
     */
    public static String category(int value) {
        if (value <= 50)  return "Good";
        if (value <= 100) return "Moderate";
        if (value <= 200) return "Unhealthy";
        if (value <= 300) return "Very Unhealthy";
        return "Hazardous";
    }
}
