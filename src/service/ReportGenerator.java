package service;

import model.Student;

import java.util.List;
import java.util.Map;

/**
 * Generates formatted text reports for individual students or the whole class.
 * Now includes a subject-wise marks breakdown with automatic grade per subject.
 */
public class ReportGenerator {

    private final GradeCalculator calculator;

    public ReportGenerator(GradeCalculator calculator) {
        this.calculator = calculator;
    }

    // ── Student report ────────────────────────────────────────────────────────

    public String generateStudentReport(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════╗\n");
        sb.append(String.format("║  Student Report  –  ID: %-3d                 ║%n", student.getId()));
        sb.append("╚══════════════════════════════════════════════╝\n");
        sb.append(String.format("  Name    : %s%n", student.getName()));

        // ── Subject-wise section ──────────────────────────────────────────────
        if (student.hasSubjectMarks()) {
            sb.append("\n  ┌─────────────────────────────────────────────┐\n");
            sb.append("  │           SUBJECT-WISE MARKS                │\n");
            sb.append("  ├────────────────────────┬────────┬───────────┤\n");
            sb.append("  │ Subject                │ Marks  │ Grade     │\n");
            sb.append("  ├────────────────────────┼────────┼───────────┤\n");

            for (Map.Entry<String, Double> entry : student.getSubjectMarks().entrySet()) {
                String subject = entry.getKey();
                double marks   = entry.getValue();
                String grade   = calculator.getLetterGrade(marks);
                String status  = marks >= 60 ? "✓ Pass" : "✗ Fail";
                sb.append(String.format("  │ %-22s │ %6.2f │ %-4s %-4s │%n",
                        subject, marks, grade, status));
            }

            sb.append("  └────────────────────────┴────────┴───────────┘\n");

            double subAvg   = calculator.calculateSubjectAverage(student);
            double total    = calculator.calculateTotalMarks(student);
            int    subjects = student.getSubjectMarks().size();

            sb.append(String.format("%n  Total Marks   : %.2f / %.0f%n",
                    total, subjects * 100.0));
            sb.append(String.format("  Percentage    : %s%n",
                    calculator.getPercentage(subAvg)));
            sb.append(String.format("  Average       : %.2f%n", subAvg));
            sb.append(String.format("  Overall Grade : %s%n",
                    calculator.getLetterGrade(subAvg)));
            sb.append(String.format("  GPA (4.0)     : %.1f%n",
                    calculator.calculateGPA(subAvg)));
            sb.append(String.format("  Best Subject  : %s%n",
                    calculator.getBestSubject(student)));
            sb.append(String.format("  Weak Subject  : %s%n",
                    calculator.getWeakestSubject(student)));
            sb.append(String.format("  Status        : %s%n",
                    calculator.isPassingSubjects(student) ? "✓ PASS" : "✗ FAIL"));
        }

        // ── Generic grades section (if any) ──────────────────────────────────
        if (!student.getGrades().isEmpty()) {
            double avg = calculator.calculateAverage(student);
            sb.append("\n  ── Generic Grades ──────────────────────────\n");
            sb.append(String.format("  Grades  : %s%n", student.getGrades()));
            sb.append(String.format("  Average : %.2f%n", avg));
            sb.append(String.format("  Highest : %.2f%n", calculator.getHighest(student)));
            sb.append(String.format("  Lowest  : %.2f%n", calculator.getLowest(student)));
            sb.append(String.format("  Grade   : %s%n", calculator.getLetterGrade(avg)));
            sb.append(String.format("  Status  : %s%n",
                    calculator.isPassing(student) ? "✓ PASS" : "✗ FAIL"));
        }

        if (!student.hasSubjectMarks() && student.getGrades().isEmpty()) {
            sb.append("\n  (No marks recorded yet)\n");
        }

        sb.append("══════════════════════════════════════════════\n");
        return sb.toString();
    }

    // ── Class report ──────────────────────────────────────────────────────────

    public String generateClassReport(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");
        sb.append("║                  CLASS SUMMARY REPORT                   ║\n");
        sb.append("╚══════════════════════════════════════════════════════════╝\n\n");
        sb.append(String.format("  Total Students : %d%n%n", students.size()));

        sb.append(String.format("  %-5s %-20s %-10s %-10s %-6s %-8s %-6s%n",
                "ID", "Name", "Total", "Avg%", "Grade", "GPA", "Status"));
        sb.append("  " + "─".repeat(70) + "\n");

        int    passing    = 0;
        double classTotal = 0;

        for (Student s : students) {
            double avg;
            double total   = 0;
            int    subjects = s.getSubjectMarks().size();

            if (s.hasSubjectMarks()) {
                avg   = calculator.calculateSubjectAverage(s);
                total = calculator.calculateTotalMarks(s);
            } else {
                avg   = calculator.calculateAverage(s);
                total = avg * s.getGrades().size();
            }

            classTotal += avg;
            boolean pass = avg >= 60.0;
            if (pass) passing++;

            sb.append(String.format("  %-5d %-20s %-10.1f %-10s %-6s %-8.1f %-6s%n",
                    s.getId(),
                    s.getName(),
                    total,
                    calculator.getPercentage(avg),
                    calculator.getLetterGrade(avg),
                    calculator.calculateGPA(avg),
                    pass ? "✓ PASS" : "✗ FAIL"));
        }

        sb.append("  " + "─".repeat(70) + "\n");
        if (!students.isEmpty()) {
            double classAvg = classTotal / students.size();
            sb.append(String.format("%n  Class Average : %.2f  (%s)%n",
                    classAvg, calculator.getLetterGrade(classAvg)));
            sb.append(String.format("  Class GPA     : %.1f%n",
                    calculator.calculateGPA(classAvg)));
        }
        sb.append(String.format("  Passing       : %d / %d%n", passing, students.size()));
        sb.append(String.format("  Failing       : %d / %d%n",
                students.size() - passing, students.size()));
        return sb.toString();
    }
}
