package einvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Program 1.8c: Validator.java
 *
 * Class description (validation module):
 *   Decides whether an invoice is acceptable and lists any problems found.
 *
 */
public class Validator {

    /**
     * Checks an invoice against the validation rules.
     *
     * @return a list of error messages; an empty list means the invoice is valid
     */
    public List<String> validate(Invoice inv) {
        
    	List<String> errors = new ArrayList<>();
        
        // Header checks: both parties need a well-formed tax ID
        if (!isValidTin(inv.supplierTin())) 
        	errors.add("Invalid supplier TIN");
        
        if (!isValidTin(inv.buyerTin()))
        	errors.add("Invalid buyer TIN");
        if (inv.items().isEmpty())          
        	errors.add("No line items");
        
        // Line checks: every item needs a positive quantity and a non-negative price
        for (LineItem item : inv.items()) {
            if (item.quantity() <= 0)  
            	errors.add("Bad quantity: " + item.description());
            
            if (item.unitPrice() < 0)  
            	errors.add("Bad price: " + item.description());
        }
        
        return errors;
    }

    /**
     * Illustrative TIN rule: 1-2 capital letters followed by 8-11 digits.
     * Private, so other classes cannot depend on how the check is done.
     */
    private boolean isValidTin(String tin) {
        
    	return tin != null && tin.matches("[A-Z]{1,2}\\d{8,11}");
    }
}