package service;

import data.StudentDatabase;
import model.Student;

import java.util.List;
import java.util.Optional;

/**
 * High-level service coordinating StudentDatabase, GradeCalculator,
 * and ReportGenerator to fulfil application use-cases.
 */
public class StudentService {

    private final StudentDatabase  database;
    private final GradeCalculator  calculator;
    private final ReportGenerator  reportGenerator;

    public StudentService() {
        this.database        = new StudentDatabase();
        this.calculator      = new GradeCalculator();
        this.reportGenerator = new ReportGenerator(calculator);
    }

    // ── Student CRUD ──────────────────────────────────────────────────────────

    public Student addStudent(String name)          { return database.addStudent(name); }
    public Optional<Student> getStudent(int id)     { return database.findById(id); }
    public List<Student> searchStudents(String kw)  { return database.findByName(kw); }
    public List<Student> getAllStudents()            { return database.getAllStudents(); }
    public boolean removeStudent(int id)            { return database.removeStudent(id); }

    // ── Generic grade management ──────────────────────────────────────────────

    public boolean addGrade(int studentId, double grade) {
        Optional<Student> opt = database.findById(studentId);
        if (opt.isEmpty()) return false;
        opt.get().addGrade(grade);
        return true;
    }

    public boolean removeGrade(int studentId, int gradeIndex) {
        Optional<Student> opt = database.findById(studentId);
        if (opt.isEmpty()) return false;
        return opt.get().removeGrade(gradeIndex);
    }

    // ── Subject-wise marks management ─────────────────────────────────────────

    /**
     * Add or update marks for a specific subject for a student.
     * Returns false if the student is not found.
     */
    public boolean addSubjectMarks(int studentId, String subject, double marks) {
        Optional<Student> opt = database.findById(studentId);
        if (opt.isEmpty()) return false;
        opt.get().addSubjectMarks(subject, marks);
        return true;
    }

    /**
     * Remove a subject entry for a student.
     */
    public boolean removeSubject(int studentId, String subject) {
        Optional<Student> opt = database.findById(studentId);
        if (opt.isEmpty()) return false;
        return opt.get().removeSubject(subject);
    }

    // ── Reports ───────────────────────────────────────────────────────────────

    public String getStudentReport(int studentId) {
        Optional<Student> opt = database.findById(studentId);
        if (opt.isEmpty()) return "Student with ID " + studentId + " not found.\n";
        return reportGenerator.generateStudentReport(opt.get());
    }

    public String getClassReport() {
        return reportGenerator.generateClassReport(database.getAllStudents());
    }

    // ── Stats helpers ─────────────────────────────────────────────────────────

    public double getAverage(int studentId) {
        return database.findById(studentId)
                .map(calculator::calculateAverage)
                .orElse(-1.0);
    }

    public double getSubjectAverage(int studentId) {
        return database.findById(studentId)
                .map(calculator::calculateSubjectAverage)
                .orElse(-1.0);
    }
}
