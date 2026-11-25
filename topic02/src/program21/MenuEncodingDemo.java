package program21;

/*
 * Program 2.1: MenuEncodingDemo.java
 *
 * Kopi Corner's counter tablets show the name of each drink in four
 * languages for international students. This program writes those names
 * to a UTF-8 text file, then compares how many characters and how many
 * bytes each name needs.
 */
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MenuEncodingDemo {

    public static void main(String[] args) throws IOException {
        // "Teh Tarik" (pulled tea) in four languages
        String[][] names = {
            {"English", "Pulled tea"},
            {"Chinese", "拉茶"},
            {"Arabic",  "شاي"},
            {"Hindi",   "चाय"}
        };

        // 1. Write the names as text, using an explicit character set
        Path file = Path.of("menu-names.txt");
        StringBuilder text = new StringBuilder();
        for (String[] row : names) {
            text.append(row[0]).append(": ").append(row[1]).append('\n');
        }
        Files.writeString(file, text.toString(), StandardCharsets.UTF_8);

        // 2. Compare characters with bytes for each name
        System.out.printf("%-8s | %-10s | %5s | %5s%n", "Language", "Name", "chars", "bytes");
        for (String[] row : names) {
            String name = row[1];
            int bytes = name.getBytes(StandardCharsets.UTF_8).length;
            System.out.printf("%-8s | %-10s | %5d | %5d%n",
                    row[0], name, name.length(), bytes);
        }

        // 3. Read the file back with the same character set
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        System.out.println("Read back " + lines.size() + " lines; file size = "
                + Files.size(file) + " bytes");
    }
}