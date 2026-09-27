package einvoice;

import java.util.List;

/**
 * This class represent a presentation module that shows the result of 
 * processing an invoice on the console.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class ReportGenerator {

    /**
     * Prints the amounts for a valid invoice, or the errors for an invalid one.
     */
    public void print(Invoice inv, List<String> errors, TaxCalculator calc) {
        
    	System.out.println("Invoice " + inv.invoiceNo());
        
    	if (errors.isEmpty()) {
            
    		System.out.printf("  Net RM%.2f | Tax RM%.2f | Gross RM%.2f "
    				+ "| ACCEPTED%n",
                    calc.calculateNet(inv), 
                    calc.calculateTax(inv), 
                    calc.calculateGross(inv));
        } else {
            
        	System.out.println("  REJECTED: " + String.join("; ", errors));
        }
    }
}
