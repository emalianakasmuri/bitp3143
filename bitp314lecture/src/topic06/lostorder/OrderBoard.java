package topic06.lostorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Program 6.1, part 1
 * 
 * This class represents the kitchen order board shared by both counter tablets 
 *  
 */
public class OrderBoard {

    // Shared among the thread, not thread-safe
    private final List<Integer> orders = new ArrayList<>();

    /**
     * This method adds one order to the end of the board
     * 
     * @param orderNo
     */
    public void post(int orderNo) {
    	
        orders.add(orderNo);
    }

    /**
     * This method returns how many orders are on the board
     * 
     * @return
     */
    public int count() {
    	
        return orders.size();
    }
}