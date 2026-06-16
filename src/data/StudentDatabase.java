package data;

import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory database for storing and retrieving Student records.
 */
public class StudentDatabase {

    private final List<Student> students = new ArrayList<>();
    private int nextId = 1;

    /** Add a new student and auto-assign an ID. Returns the created student. */
    public Student addStudent(String name) {
        Student student = new Student(nextId++, name);
        students.add(student);
        return student;
    }

    /** Find a student by ID. Returns Optional.empty() if not found. */
    public Optional<Student> findById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    /** Find students whose names contain the given keyword (case-insensitive). */
    public List<Student> findByName(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    /** Return all students. */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /** Remove a student by ID. Returns true if removed, false if not found. */
    public boolean removeStudent(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    /** Return total number of students. */
    public int size() {
        return students.size();
    }
}
