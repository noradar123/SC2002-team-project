package TestPlayground;

import entity.*;
import control.*;
import data.*;
import enums.*;
import java.time.LocalDate;

public class TESTCASE8 {

    public static void main(String[] args) {
        System.out.println("=== Testing: Student Can View Application After Visibility Toggle ===");
        System.out.println("Requirement: Students continue to have access to their application details");
        System.out.println("regardless of internship opportunities' visibility\n");

        ApplicationRepository repo = new ApplicationRepository();
        ApplicationController controller = new ApplicationController(repo);

        Student student = new Student("U1234567A", "John Doe", "password", 3, "CSC");
        CompanyRep rep = new CompanyRep("rep@company.com", "Jane", "password", "Tech Co", "HR", "Manager");

        // Create internship
        Internship internship = new Internship(
            "Software Engineering Intern",
            "Description",
            InternshipLevel.INTERMEDIATE,
            "CSC",
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            "Tech Company",
            5
        );
        internship.setStatus(InternshipStatus.APPROVED);
        internship.setVisibility(true);

        System.out.println(" INITIAL STATE:");
        System.out.println("   - Internship visibility: " + internship.isVisible());
        System.out.println("   - Internship status: " + internship.getStatus());

        // Test Step 1: Student applies while internship is visible
        System.out.println("Student applies for internship (visibility ON)...");
        Application app = controller.createApplication(student, internship);
        System.out.println("   Application created: " + app.getApplicationID());
        System.out.println("   Application status: " + app.getStatus());

        // Test Step 2: Company rep toggles visibility OFF
        System.out.println("Company rep toggles visibility OFF...");
        internship.setVisibility(false);
        System.out.println("  Visibility toggled to: " + internship.isVisible());

        // Test Step 3: Student tries to view their application
        System.out.println(" Student tries to retrieve their application...");
        Application retrieved = controller.getApplicationById(app.getApplicationID());

        // VERIFICATION - Check all application details are accessible
        System.out.println(" VERIFICATION:");
        if (retrieved != null) {
            System.out.println("   Application retrieved successfully");
            
            // Check 1: Can access application ID
            System.out.println("   Application ID: " + retrieved.getApplicationID());
            
            // Check 2: Can access student details
            System.out.println("   Student ID: " + retrieved.getStudent().getUserId());
            System.out.println("   Student Name: " + retrieved.getStudent().getName());
            
            // Check 3: Can access internship details (even though visibility is OFF)
            System.out.println("   Internship Title: " + retrieved.getInternship().getTitle());
            System.out.println("   Internship Company: " + retrieved.getInternship().getCompany());
            
            // Check 4: Can access application status
            System.out.println("   Application Status: " + retrieved.getStatus());
            System.out.println("   Application Date: " + retrieved.getApplicationDate());
            
            // Check 5: Verify internship is actually invisible
            System.out.println("Internship visibility is now: " + retrieved.getInternship().isVisible());
            
            // Final verification
            if (retrieved.getApplicationID().equals(app.getApplicationID()) &&
                retrieved.getStudent().getUserId().equals(student.getUserId()) &&
                retrieved.getInternship().getTitle().equals(internship.getTitle()) &&
                !retrieved.getInternship().isVisible()) {
                
                System.out.println("TEST PASSED: Student can access ALL application details");
                System.out.println("even though internship visibility is OFF!");
            } else {
                System.out.println("TEST FAILED: Some details are not matching");
            }
        } else {
            System.out.println("TEST FAILED: Could not retrieve application");
            System.out.println("This means students CANNOT view their applications after visibility toggle!");
        }
        
        // Additional test: Try to get applications by student
        System.out.println(" Additional check: Get all applications by student...");
        var studentApps = controller.getApplicationByStudentId(student.getUserId());
        if (studentApps != null && studentApps.size() > 0) {
            System.out.println("   Student has " + studentApps.size() + " application(s)");
            System.out.println("   Student can view their applications list");
        } else {
            System.out.println("Student cannot access their applications list");
        }
    }
}