package TestPlayground;

import entity.*;
import enums.InternshipLevel;
import enums.InternshipStatus;
import service.InternshipServiceImpl;
import store.InternshipRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TESTCASE6 {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println("==== TEST CASE 6: Internship Visibility Based on Student Eligibility ====\n");

        // -------------------------------
        // Create Career Center Staff
        // -------------------------------
        CareerCenterStaff staff = new CareerCenterStaff(
                "S001", "Admin", "pass", "SCSE", new InternshipServiceImpl(new InternshipRepository())
        );

        // Create Sample Internships (unique titles)
        Internship i1 = new Internship(
                "AI Research Intern - Level BASIC",
                "Work on AI models",
                InternshipLevel.BASIC,
                "SCSE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                "NTU Corp", 3
        );

        Internship i2 = new Internship(
                "AI Research Intern - Level ADVANCED",
                "Work on AI models",
                InternshipLevel.ADVANCED,
                "EEE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                "NTU Corp", 3
        );

        // Create Students (Year 2 and Year 4)
        Student s1 = new Student("ST001", "Alice", "pass", 2, "SCSE");
        Student s2 = new Student("ST002", "Bob", "pass", 4, "EEE");

        // Add Internships to the System
        List<Internship> internships = Arrays.asList(i1, i2);

        System.out.println("--- BEFORE Approval ---");
        printInternship(i1);
        printInternship(i2);

        // Career Center Staff APPROVES i1
        System.out.println("\n--- CCS Approves Internship 1 ---");
        staff.approveInternship(i1);  // Approve Basic Internship
        printInternship(i1); // should be APPROVED + visible true

        // Career Center Staff REJECTS i2
        System.out.println("\n--- CCS Rejects Internship 2 ---");
        staff.rejectInternship(i2);  // Reject Advanced Internship
        printInternship(i2); // should be REJECTED + visible false

        // Check Student Eligibility (with proper filtering)
        System.out.println("\n--- Eligible Internships for Alice (SCSE, Year 2) ---");
        printEligibleInternships(s1, internships);  // Only Basic Level Internships should show

        System.out.println("\n--- Eligible Internships for Bob (EEE, Year 4) ---");
        printEligibleInternships(s2, internships);  // Should show both internships

        System.out.println("\n===== END TEST CASE 6 =====");
    }

    // Method to print internship details with level information
    private static void printInternship(Internship i) {
        System.out.println("Title: " + i.getTitle());
        System.out.println("Status: " + i.getStatus());
        System.out.println("Visible: " + i.isVisible());
        System.out.println("Level: " + i.getLevel());  // Print Level here
        System.out.println("Major: " + i.getMajors());
        System.out.println("----------------------------------");
    }

    // Method to print eligible internships for a student
    private static void printEligibleInternships(Student student, List<Internship> all) {
        System.out.println("Eligible Internships for " + student.getName() + " (" + student.getMajor() + ", Year " + student.getYearOfStudy() + "):");
        for (Internship i : all) {
            if (i.canStudentApply(student, LocalDate.now())) { // Eligibility check
                System.out.println("✔ " + i.getTitle() + " (" + i.getDescription() + ")");
            }
        }
        System.out.println("----------------------------------");
    }
}
