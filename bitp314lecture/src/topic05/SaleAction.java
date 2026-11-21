package topic05;

/**
 * This interface represents what an order handler does with each cup sold at 
 * Kopi Corner. Each version of the closing report supplies its own way to 
 * record a sale.
 */
public interface SaleAction {

	// This method records one cup of this drink
	public void record(String drink);
}