# Student Grade Tracker v2.0

A Java console application to track student grades with **automatic grade calculation** based on subject-wise marks entry.

## New Features (v2.0)

- **Subject-Wise Marks Entry** – Enter marks per subject; grade is calculated instantly.
- **Automatic Grade Label** – A+, A, B+, B, C+, C, D, F assigned automatically.
- **GPA Calculation** – 4.0-scale GPA computed from average marks.
- **Percentage Display** – Shows percentage alongside letter grade.
- **Best / Weakest Subject** – Highlighted in individual student report.
- **Pass / Fail per Subject** – Each subject shows its own pass/fail status.
- **Remove Subject** – Delete a single subject from a student's record.

## Grade Scale

| Marks Range | Grade |
|-------------|-------|
| 95 – 100    | A+    |
| 90 – 94     | A     |
| 85 – 89     | B+    |
| 80 – 84     | B     |
| 75 – 79     | C+    |
| 70 – 74     | C     |
| 60 – 69     | D     |
| Below 60    | F     |

Passing threshold: **60 marks**.

## Project Structure

```
StudentGradeTracker/
├── src/
│   ├── model/
│   │   └── Student.java          # Student entity (with subjectMarks map)
│   ├── data/
│   │   └── StudentDatabase.java  # In-memory data store
│   ├── service/
│   │   ├── GradeCalculator.java  # All grade/avg/GPA computations
│   │   ├── StudentService.java   # Business logic layer
│   │   └── ReportGenerator.java  # Formatted report strings
│   ├── util/
│   │   ├── InputValidator.java   # Input sanitisation
│   │   └── FileUtil.java         # File read/write helpers
│   └── main/
│       └── Main.java             # Console UI & menu
├── reports/
│   └── StudentReport.txt
└── README.md
```

## Compiling & Running

```bash
# From the project root
javac -d out src/model/*.java src/data/*.java src/util/*.java src/service/*.java src/main/*.java
java -cp out main.Main
```

## Menu Options

| # | Option               | Description                              |
|---|----------------------|------------------------------------------|
| 1 | Add Student          | Register a new student                   |
| 2 | Add Subject Marks ★  | Enter subject + marks → auto grade shown |
| 3 | View Subject Report  | Quick table of all subjects & grades     |
| 4 | Add Generic Grade    | Legacy numeric grade entry               |
| 5 | View Student Report  | Full detailed report                     |
| 6 | View Class Report    | Summary of all students                  |
| 7 | Save Class Report    | Export to reports/StudentReport.txt      |
| 8 | Remove Student       | Delete a student record                  |
| 9 | Remove Subject       | Delete one subject from a student        |
|10 | List All Students    | Quick overview table                     |
|11 | Exit                 |                                          |
