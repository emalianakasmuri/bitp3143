package lab03.kitahub;

/** 
 * Checked exception for one broken line in a parcel manifest file. 
 */
public class ManifestRecordException extends Exception {
    
	private final int lineNo;

    public ManifestRecordException(int lineNo, String message) {
       
    	super(message);
        this.lineNo = lineNo;
    }

    public int getLineNo() {
        
    	return lineNo;
    }
}