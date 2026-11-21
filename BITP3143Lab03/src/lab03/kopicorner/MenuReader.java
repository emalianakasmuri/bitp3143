package lab03.kopicorner;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** 
 * This program reads Kopi Corner's menu file and survives broken lines. 
 * 
 */
public class MenuReader {
	
	/**
	 * Main entry point of the program
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        
    	// Input file
    	String fileName = "menu.txt";
    	
        int lineNo = 0;
        int loaded = 0;

        // try-with-resources: the reader is closed automatically, even after an error
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            
        	// Variable to hold data 
        	String line;
            
        	// Read until null, means end of fole
        	while ((line = reader.readLine()) != null) {       
        		
        		// Keeping track line number in each loop
                lineNo++;
                
                // Skip comments and empty lines
                if (line.isBlank() || line.startsWith("#")) {
                    continue;                                   
                }
                
                // Splitting data 
                String[] parts = line.split(",");
                
                try {
                	
                	// Extracting data
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    
                    System.out.printf("%-4s %-18s RM %5.2f%n", code, name, price);
                    
                    loaded++;
                    
                } catch (NumberFormatException e) {
                	// unchecked: bad number text
                    
                	System.out.println("Line " + lineNo 
                    		+ " skipped, price is not a number: " + line);
                } catch (ArrayIndexOutOfBoundsException e) {    
                	// unchecked: field missing
                    
                	System.out.println("Line " + lineNo 
                			+ " skipped, a field is missing: " + line);
                }
            }
        	
            System.out.println(loaded + " menu items loaded.");
            
        } catch (FileNotFoundException e) {                     
        	// checked: file not there
            
        	System.out.println("Menu file not found: " + fileName);
        	
        } catch (IOException e) {                               
        	// checked: any other read failure
            
        	System.out.println("Could not read the menu: " + e.getMessage());
        	
        } finally {
            
        	System.out.println("Menu reader finished.");
        }
    }
}
