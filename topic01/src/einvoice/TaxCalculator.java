package einvoice;

/**
 * Program 1.8d: TaxCalculator.java
 *
 * Class description (calculation module):
 *   Works out how much is owed on an invoice: net amount, tax and gross total.
 *
 * Note: the tax rate is illustrative.
 */
public class TaxCalculator {

    private static final double TAX_RATE = 0.08;   

    /**
     * This method calculate the sum of all line subtotals, before tax 
     *  
     */
    public double calculateNet(Invoice inv) {
        double total = 0;
        
        for (LineItem item : inv.items()) 
        	total += item.subtotal();
        
        return total;
    }

    /**
     * This method calculate  the tax on the net amount
     * 
     * @param inv
     * @return the tax on the net amount
     */
    public double calculateTax(Invoice inv)   { 
    
    	return calculateNet(inv) * TAX_RATE;
    
    }

    /**
     * This method calculate the net amount plus tax
     * 
     * @param inv
     * @return the net amount plus tax
     */
    public double calculateGross(Invoice inv) { 
    	
    	return calculateNet(inv) + calculateTax(inv); 
    }
}