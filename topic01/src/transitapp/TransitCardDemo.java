package transitapp;

/**
 * Program 1.9b: TransitCardDemo.java
 *
 * Program description:
 *   Uses the TransitCard class to simulate a passenger's day:
 *   one trip, a top-up, and an invalid top-up that is rejected.
 *
 */
public class TransitCardDemo {

    /**
     * Creates a card and demonstrates a trip, a top-up and a rejected top-up.
     */
    public static void main(String[] args) {
    	
    	System.out.println("TransitCard demonstration: A day of a passanger\n");
    	// Issue a new card with RM5.00 on it
        TransitCard card = new TransitCard("MY-0042", 5.00);
        // uses TransitCard.toString()
        System.out.println("Initial State -> " + card);                 

        // Take one trip: check entry first, then pay the fare
        System.out.println("Check first entry -> Can enter? " + card.canEnter());
        card.payFare(3.40);
        System.out.println("After trip 1: " + card);

        // Top up before the balance runs out
        System.out.println("Can enter? " + card.canEnter());
        card.topUp(20.00);
        System.out.println("After top-up: " + card);

        // An amount above the cap is rejected; the balance stays unchanged
        try {
            card.topUp(2000.00);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());   // print the reason for rejection
        }
        
        System.out.println("\nProgram ends.");
    }
}