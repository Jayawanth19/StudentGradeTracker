package util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for reading and writing report files.
 */
public class FileUtil {

    private static final String REPORTS_DIR = "reports";
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FileUtil() {}

    /**
     * Save a report string to the given filename inside the reports/ directory.
     * Prepends a timestamp header and appends a footer.
     *
     * @param filename  e.g. "StudentReport.txt"
     * @param content   the report body
     * @throws IOException if the file cannot be written
     */
    public static void saveReport(String filename, String content) throws IOException {
        Path dir = Paths.get(REPORTS_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path file = dir.resolve(filename);
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        String output = "Generated : " + timestamp + "\n\n" + content
                + "\n-- End of Report --\n";
        Files.writeString(file, output, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Report saved to: " + file.toAbsolutePath());
    }

    /**
     * Read the contents of a file from the reports/ directory.
     *
     * @param filename e.g. "StudentReport.txt"
     * @return file content as a String
     * @throws IOException if the file does not exist or cannot be read
     */
    public static String readReport(String filename) throws IOException {
        Path file = Paths.get(REPORTS_DIR, filename);
        if (!Files.exists(file)) {
            throw new FileNotFoundException("Report file not found: " + file);
        }
        return Files.readString(file);
    }

    /**
     * List all .txt files in the reports/ directory.
     */
    public static void listReports() throws IOException {
        Path dir = Paths.get(REPORTS_DIR);
        if (!Files.exists(dir)) {
            System.out.println("No reports directory found.");
            return;
        }
        System.out.println("Available reports:");
        Files.list(dir)
                .filter(p -> p.toString().endsWith(".txt"))
                .forEach(p -> System.out.println("  - " + p.getFileName()));
    }
}
