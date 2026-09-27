package einvoice;

import java.util.List;

/**
 * Program 1.8b: Invoice.java
 *
 * Record description (data module):
 *   One e-invoice: its number, the two tax IDs, and its line items.
 */
public record Invoice(String invoiceNo, String supplierTin, String buyerTin, 
	List<LineItem> items) { 
	
}