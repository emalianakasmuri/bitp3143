package previous;

/*
 * Program 2.1: ByteVsCharDemo.java
 *
 * Program description:
 *   Writes four customer names, in English, Chinese, Hindi and Arabic, to a
 *   UTF-8 text file, then compares how many characters and how many bytes
 *   each name occupies.
 *
 * What it shows:
 *   A character stream (Writer/Reader) works in characters, while a byte stream
 *   (OutputStream/InputStream) works in raw bytes. In UTF-8 one character may
 *   need 1, 2, 3 or 4 bytes, so "length" depends on which stream is used.
 *
 * Class description:
 *   ByteVsCharDemo holds the main method and a helper that counts UTF-8 bytes.
 *   Non-Latin text is written with Unicode escapes (backslash-u codes) so the
 *   source file compiles correctly whatever encoding Eclipse uses.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ByteVsCharDemo {

    /**
     * Entry point. Writes four customer names with a character stream, then
     * reads the file back with a byte stream and reports the counts.
     *
     * @param args not used
     * @throws IOException if the file cannot be written or read
     */
    public static void main(String[] args) throws IOException {
        String[] labels = {"English", "Chinese", "Hindi  ", "Arabic "};
        String[] names = {
            "Sarah",                                           // English: Sarah
            "\u738B\u5C0F\u660E",                              // Chinese: Wang Xiaoming
            "\u092A\u094D\u0930\u093F\u092F\u093E",            // Hindi:   Priya
            "\u0641\u0627\u0637\u0645\u0629"                   // Arabic:  Fatimah
        };

        Path file = Path.of("customer-names.txt");

        // Character stream: we hand the Writer characters, it encodes them as UTF-8
        try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (String name : names) {
                out.write(name);
                out.write("\n");   // fixed 1-byte line break on every OS
            }
        }

        System.out.println("Language | chars | UTF-8 bytes");
        System.out.println("---------+-------+------------");
        for (int i = 0; i < names.length; i++) {
            System.out.printf("%s  | %5d | %10d%n",
                    labels[i], names[i].length(), utf8Bytes(names[i]));
        }

        // Byte stream: read the raw file back one byte at a time and count
        long total = 0;
        try (InputStream in = Files.newInputStream(file)) {
            while (in.read() != -1) {
                total++;
            }
        }
        System.out.println("Bytes in file (incl. line breaks): " + total);
        
        System.out.println("Open the " + file.getFileName() + " to view the output");
    }

    /**
     * Counts how many bytes a string needs when encoded as UTF-8.
     *
     * @param text the text to measure
     * @return number of UTF-8 bytes
     */
    private static int utf8Bytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8).length;
    }
}
