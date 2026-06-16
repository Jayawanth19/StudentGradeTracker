package util;

/**
 * Utility class for validating user inputs before they reach the service layer.
 */
public class InputValidator {

    // Private constructor – utility class, no instantiation.
    private InputValidator() {}

    /**
     * Validate a student name.
     * Rules: not null, not blank, 2–50 characters, letters/spaces/hyphens only.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) return false;
        String trimmed = name.trim();
        return trimmed.length() >= 2
                && trimmed.length() <= 50
                && trimmed.matches("[a-zA-Z\\s\\-]+");
    }

    /**
     * Validate a numeric grade.
     * Rules: 0.0 ≤ grade ≤ 100.0
     */
    public static boolean isValidGrade(double grade) {
        return grade >= 0.0 && grade <= 100.0;
    }

    /**
     * Parse a String to a double grade.
     * Returns -1 if parsing fails or the value is out of range.
     */
    public static double parseGrade(String input) {
        try {
            double grade = Double.parseDouble(input.trim());
            return isValidGrade(grade) ? grade : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Parse a String to a positive integer (used for IDs and indexes).
     * Returns -1 on failure.
     */
    public static int parsePositiveInt(String input) {
        try {
            int value = Integer.parseInt(input.trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Sanitise a student name by trimming surrounding whitespace.
     */
    public static String sanitiseName(String name) {
        return name == null ? "" : name.trim();
    }
}
