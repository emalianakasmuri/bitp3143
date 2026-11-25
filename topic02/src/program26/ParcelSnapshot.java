package program26;

/*
 * Program 2.6: ParcelSnapshot.java
 *
 * Before the Melaka hub server restarts for maintenance, it saves the
 * parcels still waiting at the hub to a snapshot file. After the restart
 * it loads them back. A deserialization filter makes sure only the
 * expected classes can be rebuilt from the file.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParcelSnapshot {

    static class Parcel implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String parcelId;
        private final String destination;
        private final double weightKg;
        private transient String scannerSession;   // not saved

        Parcel(String parcelId, String destination, double weightKg, String scannerSession) {
            this.parcelId = parcelId;
            this.destination = destination;
            this.weightKg = weightKg;
            this.scannerSession = scannerSession;
        }

        @Override
        public String toString() {
            return parcelId + " -> " + destination + ", " + weightKg
                    + " kg, scanner session " + scannerSession;
        }
    }

    // Allow only these classes; reject everything else
    static final ObjectInputFilter FILTER = ObjectInputFilter.Config.createFilter(
            "ParcelSnapshot$Parcel;java.util.ArrayList;java.lang.*;!*");

    public static void main(String[] args) throws Exception {
        File snapshot = new File("waiting-parcels-MLK.ser");

        List<Parcel> waiting = new ArrayList<>();
        waiting.add(new Parcel("KH000101", "JB", 2.5, "SCN-07-A91"));
        waiting.add(new Parcel("KH000102", "KL", 0.8, "SCN-07-A91"));

        System.out.println("Before saving:");
        waiting.forEach(p -> System.out.println("  " + p));

        // 1. Serialize: object -> bytes
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(snapshot)))) {
            out.writeObject(waiting);
        }

        // 2. Deserialize with the filter: bytes -> object
        System.out.println("After loading:");
        for (Parcel p : load(snapshot)) {
            System.out.println("  " + p);
        }

        // 3. A file holding an unexpected class is rejected
        File strange = new File("strange.ser");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(strange))) {
            out.writeObject(new HashMap<String, String>());
        }
        try {
            load(strange);
        } catch (InvalidClassException e) {
            System.out.println("Rejected strange.ser: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    static List<Parcel> load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            in.setObjectInputFilter(FILTER);
            return (List<Parcel>) in.readObject();
        }
    }
}