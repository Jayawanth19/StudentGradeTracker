package service;

import model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Provides grade computation utilities for a Student.
 * Now supports both generic grades and subject-wise marks.
 */
public class GradeCalculator {

    // ── Generic grades (legacy) ───────────────────────────────────────────────

    public double calculateAverage(Student student) {
        List<Double> grades = student.getGrades();
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades) sum += g;
        return sum / grades.size();
    }

    public double getHighest(Student student) {
        return student.getGrades().stream()
                .mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double getLowest(Student student) {
        return student.getGrades().stream()
                .mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    public boolean isPassing(Student student) {
        return calculateAverage(student) >= 60.0;
    }

    // ── Subject-wise marks ────────────────────────────────────────────────────

    /**
     * Average of all subject marks; 0.0 if none recorded.
     */
    public double calculateSubjectAverage(Student student) {
        Collection<Double> marks = student.getSubjectMarks().values();
        if (marks.isEmpty()) return 0.0;
        return marks.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /** Highest mark across subjects; 0.0 if none. */
    public double getSubjectHighest(Student student) {
        return student.getSubjectMarks().values().stream()
                .mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    /** Lowest mark across subjects; 0.0 if none. */
    public double getSubjectLowest(Student student) {
        return student.getSubjectMarks().values().stream()
                .mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    /** Total marks across all subjects. */
    public double calculateTotalMarks(Student student) {
        return student.getSubjectMarks().values().stream()
                .mapToDouble(Double::doubleValue).sum();
    }

    /** Subject in which the student scored highest. */
    public String getBestSubject(Student student) {
        return student.getSubjectMarks().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    /** Subject in which the student scored lowest. */
    public String getWeakestSubject(Student student) {
        return student.getSubjectMarks().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    /** True if average subject marks >= 60. */
    public boolean isPassingSubjects(Student student) {
        return calculateSubjectAverage(student) >= 60.0;
    }

    // ── Shared letter-grade logic ─────────────────────────────────────────────

    /**
     * Convert a numeric average to a letter grade with +/- modifiers.
     *
     *  90-100 → A (A+ ≥ 95)
     *  80-89  → B (B+ ≥ 85)
     *  70-79  → C (C+ ≥ 75)
     *  60-69  → D
     *  below  → F
     */
    public String getLetterGrade(double average) {
        if (average >= 95) return "A+";
        if (average >= 90) return "A";
        if (average >= 85) return "B+";
        if (average >= 80) return "B";
        if (average >= 75) return "C+";
        if (average >= 70) return "C";
        if (average >= 60) return "D";
        return "F";
    }

    /**
     * GPA on a 4.0 scale derived from average marks.
     */
    public double calculateGPA(double average) {
        if (average >= 90) return 4.0;
        if (average >= 80) return 3.0;
        if (average >= 70) return 2.0;
        if (average >= 60) return 1.0;
        return 0.0;
    }

    /**
     * Percentage string for display (e.g. "87.50%").
     */
    public String getPercentage(double average) {
        return String.format("%.2f%%", average);
    }
}
