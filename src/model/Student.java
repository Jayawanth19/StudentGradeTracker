package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a student with an ID, name, generic grades list,
 * and a subject-wise marks map for automatic grade calculation.
 */
public class Student {

    private int id;
    private String name;
    private List<Double> grades;                     // legacy generic grades
    private Map<String, Double> subjectMarks;        // subject → marks (0-100)

    public Student(int id, String name) {
        this.id           = id;
        this.name         = name;
        this.grades       = new ArrayList<>();
        this.subjectMarks = new LinkedHashMap<>();
    }

    // ── Basic getters / setters ───────────────────────────────────────────────

    public int getId()             { return id; }
    public String getName()        { return name; }
    public List<Double> getGrades(){ return grades; }
    public void setName(String name){ this.name = name; }

    // ── Legacy grade list operations ──────────────────────────────────────────

    public void addGrade(double grade) { grades.add(grade); }

    public boolean removeGrade(int index) {
        if (index >= 0 && index < grades.size()) {
            grades.remove(index);
            return true;
        }
        return false;
    }

    // ── Subject-wise marks operations ─────────────────────────────────────────

    /** Add or update marks for a subject. */
    public void addSubjectMarks(String subject, double marks) {
        subjectMarks.put(subject.trim(), marks);
    }

    /** Remove a subject entry. Returns true if it existed. */
    public boolean removeSubject(String subject) {
        return subjectMarks.remove(subject.trim()) != null;
    }

    /** Returns an unmodifiable view of subject → marks. */
    public Map<String, Double> getSubjectMarks() {
        return subjectMarks;
    }

    /** True if at least one subject mark has been recorded. */
    public boolean hasSubjectMarks() {
        return !subjectMarks.isEmpty();
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', grades=" + grades
                + ", subjectMarks=" + subjectMarks + "}";
    }
}
