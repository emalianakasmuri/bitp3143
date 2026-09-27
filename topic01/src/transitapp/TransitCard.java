package transitapp;

/**
* This class holds a card number and a balance, with methods to top up, 
* check entry, pay a fare and read the balance.
*
* The class demonstrate the following:-
*   - High cohesion: every field and method serves one job, managing the
*     card's stored value, and every method works on the same field, balance.
*   - Encapsulation: balance is private and can only change through the
*     rules in topUp() and payFare().
*
* @author Emaliana Kasmuri FTMK
* for BITP 3143 Distributed and Parallel Application Development   
*/
public class TransitCard {

   // Values are in RM
   private static final double MAX_BALANCE = 1500.00;   
   private static final double MIN_TO_ENTER = 1.00;     

   // Fixed once the card is issued
   private final String cardNo;  
   
   // Stored value in RM
   private double balance;       

   /**
    * Creates a card with the given number and opening balance.
    *
    * @param cardNo         the card number printed on the card
    * @param openingBalance the starting balance in RM
    */
   public TransitCard(String cardNo, double openingBalance) {
       this.cardNo = cardNo;
       this.balance = openingBalance;
   }

   /**
    * This method adds value to the card.
    *
    * @param amount the top-up amount in RM
    * @throws IllegalArgumentException if the amount is not positive
    *         or would take the balance above the cap
    */
   public void topUp(double amount) {
	   
       // Reject a zero or negative amount, or one that exceeds the maximum 
	   // value
       if (amount <= 0 || balance + amount > MAX_BALANCE) {
           throw new IllegalArgumentException("Top-up rejected: RM" + amount);
       }
       
       balance += amount;
   }

   /**
    * This method checks whether the card can be used to enter the station.
    *
    * @return true if the balance is at least the minimum needed to tap in
    */
   public boolean canEnter() {
       
	   return balance >= MIN_TO_ENTER;
   }

   /**
    * This method deducts a trip fare from the card.
    *
    * @param fare the fare for the trip in RM
    * @throws IllegalStateException if the balance cannot cover the fare
    */
   public void payFare(double fare) {
       
	   // Never let the balance go negative
       if (fare > balance) {
           throw new IllegalStateException("Insufficient balance for fare RM" +
        		   fare);
       }
       
       balance -= fare;
   }

   /**
    * This method returns the current balance.
    *
    * @return the balance in RM
    */
   public double getBalance() {
       
	   return balance;
   }

   /**
    * This method returns the card number and balance, formatted for printing.
    * Called automatically when the card is printed with println().
    */
   @Override
   public String toString() {
       
	   return String.format("Card %s: RM%.2f", cardNo, balance);
   }
}
