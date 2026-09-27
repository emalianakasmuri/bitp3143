package utilityapp;

/**
 * This program calculates the monthly electricity bill for three households 
 * using tiered (block) pricing, where each additional block of kWh costs more.
 * For each household it prints the charge for every block used and the total.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */

public class TieredBill {
	
	// Each block: size in kWh (last block is unlimited) and rate in RM per kWh
    static final int[]    BLOCK_SIZE = {200, 100, 300, Integer.MAX_VALUE};
    static final double[] BLOCK_RATE = {0.22, 0.33, 0.52, 0.55};
    
    /**
     * This method Calculates one household's bill by charging its usage block 
     * by block.
     * Prints the charge for each block used and returns the total in RM.
     *
     * @param usageKwh electricity used in the month, in kWh
     * @return the total bill in RM
     */
    static double bill(int usageKwh) {
    	
    	// Running total of the bill in RM
    	double total = 0.0;
    	
    	// kWh still to be charged
        int remaining = usageKwh;    

        // Visit the blocks in order
        for (int i = 0; i < BLOCK_SIZE.length; i++) {
            
        	// All usage has been charged, so the remaining blocks are not 
        	// needed
            if (remaining <= 0) {
            	
            	// nothing left to charge
                break;                              
            }
           
            // Charge only what fits in this block: the smaller of what is left
            // and the block size
            int units = Math.min(remaining, BLOCK_SIZE[i]);
            double charge = units * BLOCK_RATE[i];
            
            System.out.printf("  Block %d: %4d kWh x RM%.2f = RM%7.2f%n",
                    i + 1, units, BLOCK_RATE[i], charge);
            // Add this block's charge to the bill
            total += charge;
            
            // These units are now paid for
            remaining -= units;     
        }
        
        return total;
    }
    
    /**
     * Main entry point to the program
     */
    public static void main(String[] args) {
    	
    	System.out.println("Sample of utility charges by block\n");
    	
    	// Sample household usage data
        int[] households = {150, 450, 820};
        
        // Process utility charges
        for (int usage : households) {
            System.out.println("Usage " + usage + " kWh:");
            System.out.printf("  Total = RM%.2f%n%n", bill(usage));
        }
        
        System.out.println("\nProgram ends");
    }
}

