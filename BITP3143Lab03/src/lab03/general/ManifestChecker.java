package lab03.kitahub;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/** 
 * Exercise 5: Checks a KitaHub manifest and splits it into valid records 
 * and an error log. 
 */
public class ManifestChecker {

	// Creates a fixed set of hub codes
    static final Set<String> HUBS = Set.of("KUL", "MLK", "JHR");
    static final double MAX_WEIGHT_KG = 30.0;

    /** 
     * Validates one manifest line. Returns a ParcelRecord or 
     * throws ManifestRecordException. 
     */
    public static ParcelRecord parseRecord(String line, int lineNo) 
    		throws ManifestRecordException {
        
    	String[] parts = line.split(",");
        
        // TODO 1: if parts.length is not 4, throw new ManifestRecordException(
    	//         lineNo, "expected 4 fields but found " + parts.length)
        
        // TODO 2: trim the parcel ID, origin and destination. Check origin and 
    	//         destination against HUBS.
        
        // TODO 3: parse the weight with Double.parseDouble(). 
    	//         Turn a NumberFormatException into a
        //         ManifestRecordException. Reject weights <= 0 or > MAX_WEIGHT_KG.
        
        // TODO 4: return new ParcelRecord(id, origin, destination, weight)
        
    	return null;  // replace this line
    }

    public static void main(String[] args) {
        
    	String inFile = (args.length > 0) ? args[0] : "manifest_MLK_001.txt";
        String validFile = inFile.replace(".txt", "_valid.txt");
        String errorFile = "manifest_errors.log";
        int lineNo = 0, valid = 0, rejected = 0;

        // Three resources in one try-with-resources; all three are closed at the end
        try (BufferedReader reader = new BufferedReader(new FileReader(inFile));
             PrintWriter validOut = new PrintWriter(new FileWriter(validFile));
             PrintWriter errorOut = new PrintWriter(new FileWriter(errorFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank() || line.startsWith("#")) continue;
                // TODO 5: call parseRecord(line, lineNo) inside try-catch.
                //         Valid: write parcel.toLine() to validOut and 
                //         add 1 to valid.
                //         Invalid: write "Line <n>: <message> -> <line>" to 
                //         errorOut and add 1 to rejected.
            }
            System.out.println("Checked " + inFile + ": " + (valid + rejected) 
            		+ " records, "
                    + valid + " valid, " + rejected + " rejected.");
            System.out.println("Valid records: " + validFile + "   Errors: " + errorFile);
        
        } catch (FileNotFoundException e) {
            
        	System.out.println("Manifest not found: " + inFile + ". Check the file name and try again.");
        
        } catch (IOException e) {
            
        	System.out.println("I/O problem while checking the manifest: " + e.getMessage());
        }
    }
}