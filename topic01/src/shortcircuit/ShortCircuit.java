package shortcircuit;

/**
 * This program demonstrates short-circuit evaluation using a simple e-wallet 
 * payment check.  It evaluates the same condition, "wallet is active AND 
 * balance is enough", in two ways and then performs a null-safe promo-code 
 * check.
 * 
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development   
 */
public class ShortCircuit {

    // Counts how many times hasBalance() actually runs
    static int checks = 0;

    // A condition with a visible side effect: it prints a message
    // and increases the counter every time it is evaluated
    static boolean hasBalance(double balance, double amount) {
    	
        checks++;
        
        System.out.println("  -> hasBalance() called");
        
        return balance >= amount;
    }

    /**
     * Main entry point if the program
     * 
     * @param args
     */
    public static void main(String[] args) {
    	
    	System.out.println("Sample output from ShortCircuit.java\n");
    	
    	
        // The wallet is inactive, so any AND with it must be false
        boolean walletActive = false;
        double balance = 50.00, amount = 20.00;

        // Case 1: && sees that the left side is false and skips the right side,
        // so hasBalance() is never called and checks stays at 0
        System.out.println("Using && (short-circuit):");
        boolean ok1 = walletActive && hasBalance(balance, amount);
        System.out.println("  result = " + ok1 + ", checks so far = " + checks);

        // Case 2: & always evaluates both sides, even though the answer is
        // already known, so hasBalance() runs and checks rises to 1
        System.out.println("Using & (no short-circuit):");
        boolean ok2 = walletActive & hasBalance(balance, amount);
        System.out.println("  result = " + ok2 + ", checks so far = " + checks);

        // Case 3: short-circuit as a guard against a NullPointerException.
        // promoCode is null, so && stops before calling startsWith() on it
        String promoCode = null;
        boolean valid = promoCode != null && promoCode.startsWith("MERDEKA");
        System.out.println("Promo code valid? " + valid);
        
        System.out.println("\nProgram ends");
    }
}
