package program23;

/*
 * Program 2.3: MenuLoader.java
 *
 * Reads Kopi Corner's menu file (data/menu.csv) and writes a price list
 * for the counter tablets (data/price-list.txt).
 * Each menu line has three fields: code,name,price
 */
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MenuLoader {

    public static void main(String[] args) throws IOException {
        Path folder = Path.of("data");
        Path menuFile = folder.resolve("menu.csv");
        Path priceList = folder.resolve("price-list.txt");

        createSampleMenu(folder, menuFile);   // setup for this example only

        // 1. The menu is small, so load every line into memory at once
        List<String> lines = Files.readAllLines(menuFile, StandardCharsets.UTF_8);

        // 2. Write the price list line by line through a buffered writer
        try (BufferedWriter out =
                     Files.newBufferedWriter(priceList, StandardCharsets.UTF_8)) {
            out.write("KOPI CORNER PRICE LIST");
            out.newLine();
            for (String line : lines.subList(1, lines.size())) {   // skip header
                String[] f = line.split(",");
                String code = f[0];
                String name = f[1];
                BigDecimal price = new BigDecimal(f[2]);
                out.write(String.format("%-4s %-20s RM %6s", code, name, price));
                out.newLine();
            }
        }   // the writer is flushed and closed here

        System.out.println("Read " + (lines.size() - 1) + " menu items from " + menuFile);
        System.out.println("Price list written to " + priceList);
        System.out.println();
        Files.readAllLines(priceList).forEach(System.out::println);
    }

    static void createSampleMenu(Path folder, Path menuFile) throws IOException {
        Files.createDirectories(folder);
        Files.write(menuFile, List.of(
                "code,name,price",
                "D01,Kopi O,2.50",
                "D02,Teh Tarik,2.80",
                "N01,Mee Goreng,6.50",
                "N02,Kuey Teow Sup,7.00",
                "T01,Roti Bakar Kaya,3.50",
                "T02,Telur Separuh Masak,3.00"), StandardCharsets.UTF_8);
    }
}