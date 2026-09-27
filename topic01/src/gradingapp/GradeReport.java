package gradingapp;

/**
 * This program converts the marks of four students into letter grades and 
 * prints a short report with a remark for each student.
 *
 * @author Emaliana Kasmuri FTMK
 * for BITP 3143 Distributed and Parallel Application Development    
 */

public class GradeReport {
	
	/**
	 * This method returns grade based on the student mark
	 * 
	 * @param mark
	 * @return
	 */
	 static String grade(int mark) {
		 
		 	// A good practice: Block starts by filtering out invalid input
		 	
	        if (mark < 0 || mark > 100) {
	            throw new IllegalArgumentException("Mark out of range: " + mark);
	        } else if (mark >= 80) {
	            return "A";
	        } else if (mark >= 70) {
	            return "B";
	        } else if (mark >= 60) {
	            return "C";
	        } else if (mark >= 50) {
	            return "D";
	        } else {
	            return "E";
	        }
	    }

	 	/**
	 	 * This method interprets a grade
	 	 * @param grade
	 	 * @return
	 	 */
	    static String remark(String grade) {
	        
	    	// Switch expression (Java 14+): no fall-through, must cover every case
	        return switch (grade) {
	            case "A", "B" -> "Good standing";
	            case "C", "D" -> "Pass";
	            case "E"      -> "Repeat course";
	            default       -> throw new IllegalStateException("Unknown grade " + grade);
	        };
	    }
	    
	    /**
	     * Main entry point of the program
	     * 
	     * @param args
	     */
	    public static void main(String[] args) {
	    	
	    	// Program info
	    	System.out.println("BITP 3143: Lab Assessment Result\n");
	    	
	    	// Print heading
	    	System.out.println("Name\t  Marks\tGrade\tStatus");
	    	System.out.println("----\t  -----\t-----\t------");
	    	
	    	
	    	// Student data and their marks
	        String[] names = {"Aina", "Wei Jie", "Kavitha", "Hafiz"};
	        int[] marks    = {86, 72, 55, 41};
	        
	        // Grading process 
	        for (int i = 0; i < names.length; i++) {
	            String g = grade(marks[i]);
	            System.out.printf("%-8s %3d\t%s\t%s%n", names[i], marks[i], g, remark(g));
	        }
	        
	        System.out.println("\n\nProgram Ends");
	    }

}
