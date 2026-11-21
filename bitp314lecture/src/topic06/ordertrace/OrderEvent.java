package topic06.ordertrace;

/**
 * Program 6.3, part 1
 * 
 * This record holds one trace event: when it happened, which thread, 
 * which order, and what the thread saw.
 */
public record OrderEvent(long nanos, String thread, int orderNo, 
	int boardSizeSeen) { 
	
	
}