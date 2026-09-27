package campusmgmt;

public class CampusManagerDemo {
	
	public static void main(String[] args) {
        CampusManager m = new CampusManager();
        m.addMark(78); m.addMark(64); m.addMark(91);
        m.recordOffence(); m.recordOffence();
        m.recordSale(8.50); m.recordSale(12.00);

        System.out.printf("Average mark : %.2f%n", m.averageMark());
        System.out.printf("Parking fines: RM%.2f%n", m.totalFines());
        System.out.printf("Cafe sales   : RM%.2f%n", m.salesToday());
    }


}
