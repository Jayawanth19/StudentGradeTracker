package main;

import model.Student;
import service.StudentService;
import util.FileUtil;
import util.InputValidator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Entry point for the Student Grade Tracker console application.
 *
 * Menu structure
 * ──────────────
 *  1.  Add Student
 *  2.  Add Subject Marks  (NEW – enter subject name + marks → auto grade)
 *  3.  View Subject Marks Report (NEW)
 *  4.  Add Generic Grade
 *  5.  View Student Report
 *  6.  View Class Report
 *  7.  Save Class Report to File
 *  8.  Remove Student
 *  9.  Remove Subject      (NEW)
 *  10. List All Students
 *  11. Exit
 */
public class Main {

    private static final StudentService service = new StudentService();
    private static final Scanner        scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      Student Grade Tracker  v2.0         ║");
        System.out.println("║  Now with Subject-Wise Auto Grading! 🎓  ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1"  -> addStudent();
                case "2"  -> addSubjectMarks();
                case "3"  -> viewSubjectMarksReport();
                case "4"  -> addGrade();
                case "5"  -> viewStudentReport();
                case "6"  -> viewClassReport();
                case "7"  -> saveClassReport();
                case "8"  -> removeStudent();
                case "9"  -> removeSubject();
                case "10" -> listAllStudents();
                case "11" -> {
                    System.out.println("\n  Goodbye! 👋");
                    running = false;
                }
                default -> System.out.println("  ⚠  Invalid choice. Please enter 1-11.");
            }
        }
    }

    // ── Menu ──────────────────────────────────────────────────────────────────

    private static void printMenu() {
        System.out.println("""

                ┌────────────────────────────────────────┐
                │                MENU                    │
                ├────────────────────────────────────────┤
                │  1.  Add Student                       │
                │  2.  Add Subject Marks  ★ AUTO GRADE   │
                │  3.  View Subject Marks Report         │
                │  4.  Add Generic Grade                 │
                │  5.  View Full Student Report          │
                │  6.  View Class Report                 │
                │  7.  Save Class Report to File         │
                │  8.  Remove Student                    │
                │  9.  Remove Subject                    │
                │  10. List All Students                 │
                │  11. Exit                              │
                └────────────────────────────────────────┘
                Enter choice:\s""");
    }

    // ── Handlers ──────────────────────────────────────────────────────────────

    private static void addStudent() {
        System.out.print("  Enter student name: ");
        String name = scanner.nextLine();
        if (!InputValidator.isValidName(name)) {
            System.out.println("  ⚠  Invalid name. Use letters, spaces, or hyphens (2-50 chars).");
            return;
        }
        Student s = service.addStudent(InputValidator.sanitiseName(name));
        System.out.printf("  ✓ Student added → ID: %d, Name: %s%n", s.getId(), s.getName());
    }

    // ── NEW: Subject marks entry with automatic grade ─────────────────────────

    private static void addSubjectMarks() {
        System.out.print("  Enter student ID: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }

        Optional<Student> opt = service.getStudent(id);
        if (opt.isEmpty()) { System.out.println("  ⚠  Student not found."); return; }

        System.out.printf("  Adding subject marks for: %s (ID: %d)%n",
                opt.get().getName(), id);
        System.out.println("  (Type 'done' as subject name to finish)\n");

        int count = 0;
        while (true) {
            System.out.print("  Subject name  : ");
            String subject = scanner.nextLine().trim();
            if (subject.equalsIgnoreCase("done")) break;
            if (subject.isEmpty()) {
                System.out.println("  ⚠  Subject name cannot be empty.");
                continue;
            }

            System.out.printf("  Marks for %-20s (0-100): ", subject);
            double marks = InputValidator.parseGrade(scanner.nextLine());
            if (marks == -1) {
                System.out.println("  ⚠  Invalid marks. Must be 0-100. Try again.");
                continue;
            }

            service.addSubjectMarks(id, subject, marks);

            // Instant feedback – show auto-calculated grade
            String grade  = gradeLabel(marks);
            String status = marks >= 60 ? "✓ Pass" : "✗ Fail";
            System.out.printf("  ✓ %-20s → %.2f  │  Grade: %-3s │ %s%n",
                    subject, marks, grade, status);
            count++;
        }

        if (count > 0) {
            double avg = service.getSubjectAverage(id);
            System.out.printf("%n  ── Summary for %s ──%n", opt.get().getName());
            System.out.printf("  Subjects entered  : %d%n", count);
            System.out.printf("  Average           : %.2f%n", avg);
            System.out.printf("  Overall Grade     : %s%n", gradeLabel(avg));
            System.out.printf("  Status            : %s%n%n", avg >= 60 ? "✓ PASS" : "✗ FAIL");
        }
    }

    /** View a quick subject-marks table for one student. */
    private static void viewSubjectMarksReport() {
        System.out.print("  Enter student ID: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }

        Optional<Student> opt = service.getStudent(id);
        if (opt.isEmpty()) { System.out.println("  ⚠  Student not found."); return; }

        Student s = opt.get();
        if (!s.hasSubjectMarks()) {
            System.out.println("  (No subject marks recorded for this student yet.)");
            return;
        }

        System.out.printf("%n  ═══ Subject Marks: %s (ID %d) ═══%n", s.getName(), s.getId());
        System.out.printf("  %-24s %8s %6s %8s %s%n", "Subject", "Marks", "Grade", "Status", "");
        System.out.println("  " + "─".repeat(55));

        for (Map.Entry<String, Double> e : s.getSubjectMarks().entrySet()) {
            double marks  = e.getValue();
            String grade  = gradeLabel(marks);
            String status = marks >= 60 ? "✓ Pass" : "✗ Fail";
            System.out.printf("  %-24s %8.2f %6s %8s%n",
                    e.getKey(), marks, grade, status);
        }

        System.out.println("  " + "─".repeat(55));
        double avg    = service.getSubjectAverage(id);
        System.out.printf("  Overall Average : %.2f%n", avg);
        System.out.printf("  Overall Grade   : %s%n", gradeLabel(avg));
        System.out.printf("  Status          : %s%n%n", avg >= 60 ? "✓ PASS" : "✗ FAIL");
    }

    // ── Existing handlers (unchanged functionality) ───────────────────────────

    private static void addGrade() {
        System.out.print("  Enter student ID: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }

        System.out.print("  Enter grade (0-100): ");
        double grade = InputValidator.parseGrade(scanner.nextLine());
        if (grade == -1) { System.out.println("  ⚠  Invalid grade. Must be 0-100."); return; }

        if (service.addGrade(id, grade)) {
            System.out.printf("  ✓ Grade %.2f added to student ID %d%n", grade, id);
        } else {
            System.out.println("  ⚠  Student not found.");
        }
    }

    private static void viewStudentReport() {
        System.out.print("  Enter student ID: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }
        System.out.println(service.getStudentReport(id));
    }

    private static void viewClassReport() {
        System.out.println(service.getClassReport());
    }

    private static void saveClassReport() {
        try {
            FileUtil.saveReport("StudentReport.txt", service.getClassReport());
        } catch (IOException e) {
            System.out.println("  ⚠  Could not save report: " + e.getMessage());
        }
    }

    private static void removeStudent() {
        System.out.print("  Enter student ID to remove: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }
        System.out.println(service.removeStudent(id) ? "  ✓ Student removed." : "  ⚠  Student not found.");
    }

    private static void removeSubject() {
        System.out.print("  Enter student ID: ");
        int id = InputValidator.parsePositiveInt(scanner.nextLine());
        if (id == -1) { System.out.println("  ⚠  Invalid ID."); return; }

        System.out.print("  Enter subject name to remove: ");
        String subject = scanner.nextLine().trim();
        if (subject.isEmpty()) { System.out.println("  ⚠  Subject name cannot be empty."); return; }

        System.out.println(service.removeSubject(id, subject)
                ? "  ✓ Subject '" + subject + "' removed."
                : "  ⚠  Subject not found for this student.");
    }

    private static void listAllStudents() {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) { System.out.println("  (no students yet)"); return; }

        System.out.printf("  %-5s %-22s %-10s %-10s%n", "ID", "Name", "Subjects", "Avg Grade");
        System.out.println("  " + "─".repeat(52));
        students.forEach(s -> {
            double avg = s.hasSubjectMarks()
                    ? service.getSubjectAverage(s.getId())
                    : service.getAverage(s.getId());
            System.out.printf("  %-5d %-22s %-10d %-10s%n",
                    s.getId(), s.getName(),
                    s.getSubjectMarks().size(),
                    s.hasSubjectMarks() ? gradeLabel(avg) : "(generic)");
        });
        System.out.println();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /** Shortcut so menu code doesn't need a separate service call. */
    private static String gradeLabel(double marks) {
        if (marks >= 95) return "A+";
        if (marks >= 90) return "A";
        if (marks >= 85) return "B+";
        if (marks >= 80) return "B";
        if (marks >= 75) return "C+";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }
}
