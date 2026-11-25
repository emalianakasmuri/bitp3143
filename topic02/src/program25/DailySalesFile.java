package program25;

/*
 * Program 2.5: DailySalesFile.java
 *
 * At closing time, Kopi Corner's kitchen server saves the day's sales in a
 * compact binary file. The program writes the file with DataOutputStream,
 * reads it back correctly with DataInputStream, then reads it again with
 * the fields in the wrong order to show what goes wrong.
 *
 * File layout (the reader must follow exactly the same order):
 *   long  date as a day number (LocalDate.toEpochDay)
 *   int   number of items
 *   then, for each item:
 *     UTF  item code
 *     int  quantity sold
 *     int  revenue in sen (RM 1.00 = 100 sen)
 */
import java.io.*;
import java.time.LocalDate;

public class DailySalesFile {

    public static void main(String[] args) throws IOException {
        File file = new File("sales-2026-10-08.dat");
        String[] codes   = {"D01", "D02", "N01"};
        int[]    qty     = {120, 95, 64};
        int[]    revenue = {30000, 26600, 41600};      // in sen

        // 1. Write
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeLong(LocalDate.of(2026, 10, 8).toEpochDay());
            out.writeInt(codes.length);
            for (int i = 0; i < codes.length; i++) {
                out.writeUTF(codes[i]);
                out.writeInt(qty[i]);
                out.writeInt(revenue[i]);
            }
        }
        System.out.println("File size: " + file.length() + " bytes");

        // 2. Read in the same order as written
        System.out.println("\n--- Correct order ---");
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            LocalDate date = LocalDate.ofEpochDay(in.readLong());
            int count = in.readInt();
            System.out.println("Sales for " + date + ", " + count + " items");
            for (int i = 0; i < count; i++) {
                String code = in.readUTF();
                int q = in.readInt();
                int sen = in.readInt();
                System.out.printf("%s  qty %3d  RM %d.%02d%n", code, q, sen / 100, sen % 100);
            }
        }

        // 3. Read with the first field as an int instead of a long
        System.out.println("\n--- Wrong order: date read as int ---");
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            int wrongDate = in.readInt();     // only half of the long
            int wrongCount = in.readInt();    // the other half of the long
            System.out.println("date field  = " + wrongDate);
            System.out.println("item count  = " + wrongCount);
            String code = in.readUTF();       // now reading from the wrong place
            System.out.println("first code  = \"" + code + "\"");
        }   // no exception: the wrong values look like ordinary data
    }
}