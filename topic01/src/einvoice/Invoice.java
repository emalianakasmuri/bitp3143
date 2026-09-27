package einvoice;

import java.util.List;

/**
 * This record represents a data module for one e-invoice information - 
 * 	its number, the two tax IDs, and its line items.
 *   
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development 
 */
public record Invoice(String invoiceNo, String supplierTin, String buyerTin, 
	List<LineItem> items) { 
	
}