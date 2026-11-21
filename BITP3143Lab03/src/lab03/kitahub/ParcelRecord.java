package lab03.kitahub;

/** 
 * One valid parcel line from a KitaHub manifest. 
 * A record is a short way to write a class that only holds data. 
 * */
public record ParcelRecord(String parcelId, String origin, 
		String destination, double weightKg) {

    /** 
     * Back to the manifest line format: id,origin,destination,weight 
     * 
     */
    public String toLine() {
        return parcelId + "," + origin + "," + destination + "," + weightKg;
    }
}
