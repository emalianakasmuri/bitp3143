/*
 * Lab 04, Exercise 4: MenuServer.java
 *
 * Program description:
 *   Plays the Kopi Corner kitchen server handing out the menu. It reads
 *   menu.txt (from Lab 03) into a list of MenuItem records, skipping broken
 *   lines. A tablet sends the request "MENU" as a String object, and the
 *   server replies with the whole list as one object. It serves one tablet,
 *   then ends.
 *
 * What it shows:
 *   Reading a file with streams (Lab 03), then sending a whole collection of
 *   objects with ObjectOutputStream, and the stream-header rule.
 *
 * How to run:
 *   Put menu.txt in the project folder, beside src. Start this program first,
 *   then run MenuTablet.
 */
package lab04.kopicorner.solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MenuServer {

    /** Port number on which the menu server listens. */
    private static final int PORT = 5102;

    /**
     * Loads the menu, then sends it to one tablet.
     *
     * @param args not used
     */
    public static void main(String[] args) {

    	// Load data from menu.txt
        ArrayList<MenuItem> menu = loadMenu("menu.txt");
        
        // Initial message indicating server is running
        System.out.println("[Kitchen] Loaded " + menu.size() + " menu items");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Kitchen] Menu server waiting on port " + PORT);

            try (Socket tablet = serverSocket.accept();
                 ObjectOutputStream out = new ObjectOutputStream(
                		 tablet.getOutputStream())) {
            	
            	// stream header first
                out.flush();                                       
                
                try (ObjectInputStream in = new ObjectInputStream(
                		tablet.getInputStream())) {
                	
                    Object request = in.readObject();
                    System.out.println("[Kitchen] Request received: " + request);
                    
                    if ("MENU".equals(request)) {
                    	// the whole list as one object
                        out.writeObject(menu);                     
                    } else {
                        out.writeObject("400 BAD_REQUEST");
                    }
                    out.flush();
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
        	
            System.err.println("[Kitchen] Problem: " + e);
        }
        System.out.println("[Kitchen] Finished");
    }

    /**
     * Reads the menu file, skipping comments and broken lines.
     *
     * @param fileName the menu file
     * @return the valid menu items, in file order
     */
    private static ArrayList<MenuItem> loadMenu(String fileName) {
       
    	ArrayList<MenuItem> menu = new ArrayList<>();
        
    	try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            
    		String line;
            
    		while ((line = reader.readLine()) != null) {
            
    			if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                
    			try {
                
    				String[] parts = line.split(",");
                    int priceSen = (int) Math.round(Double.parseDouble(parts[2]) * 100);
                    menu.add(new MenuItem(parts[0].trim(), parts[1].trim(), priceSen));
                    
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    
                	System.out.println("[Kitchen] Skipped broken line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[Kitchen] Cannot read " + fileName + ": " + e.getMessage());
        }
        return menu;
    }
}
