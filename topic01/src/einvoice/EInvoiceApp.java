package einvoice;

import java.util.List;

/**
 * This program validates a small set of e-invoices and, for each valid one, 
 * calculates the net amount, tax and gross total. Invalid invoices are 
 * rejected with a list of the problems found.
 *
 * The program demonstrate the following:-
 *   - Modular design: each class has one responsibility (data, validation,
 *     calculation, presentation), and main() only coordinates them.
 *   - Information hiding: private details such as the TIN rule and tax rate
 *     can change without affecting the other classes.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development
 */
public class EInvoiceApp {

    /**
     * Program main entry point
     * 
     * It builds three test invoices, then validates, calculates and reports 
     * each one.
     * main() coordinates the modules and contains no business rules.
     */
    public static void main(String[] args) {
        
    	// Test data: INV-001 and INV-003 are valid; INV-002 has two errors
        List<Invoice> batch = List.of(
            new Invoice("INV-001", "C2584563200", "IG11223344",
                List.of(new LineItem("Cloud hosting (1 month)", 1, 450.00),
                        new LineItem("SSL certificate", 2, 60.00))),
            new Invoice("INV-002", "C2584563200", "12345",
                List.of(new LineItem("IoT sensor", 0, 85.00))),
            new Invoice("INV-003", "C9988776655", "C1122334455",
                List.of(new LineItem("Data annotation service", 40, 12.50)))
        );

        // Create one object for each module
        Validator validator = new Validator();
        TaxCalculator calc  = new TaxCalculator();
        ReportGenerator reportGenerator   = new ReportGenerator();

        // Pass each invoice through the modules in turn
        for (Invoice inv : batch) {
        	reportGenerator.print(inv, validator.validate(inv), calc);
        }
        
        System.out.println("\n\nProgram ends.");
    }
}
