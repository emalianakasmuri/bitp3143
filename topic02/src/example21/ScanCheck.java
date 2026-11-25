package example21;

/*
 * Example 2.1: ScanCheck.java
 * Good scans go to System.out; problems go to System.err.
 */
public class ScanCheck {
    public static void main(String[] args) {
        String[] scans = {"KH000301,JB", "KH000302,", "KH000303,KL"};
        for (String scan : scans) {
            String[] f = scan.split(",");
            if (f.length == 2) {
                System.out.println(scan);                         // result data
            } else {
                System.err.println("ERROR missing destination: " + scan);  // diagnostic
            }
        }
    }
}