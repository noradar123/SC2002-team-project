package TestPlayground;

import entity.*;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.time.LocalDate;
import java.util.Arrays;
import service.InternshipServiceImpl;
import store.InternshipRepository;

public class TESTCASE21 {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println("===== TEST CASE 21: Internship Opportunity Approval =====\n");

        // ------------------------------- Create Career Center Staff -------------------------------
        InternshipRepository internshipRepo = new InternshipRepository();  // Create your Internship repository
        InternshipServiceImpl internshipService = new InternshipServiceImpl(internshipRepo);  // Inject repo into the service

        CareerCenterStaff staff = new CareerCenterStaff(
                "S001",           // userId
                "Admin",          // name
                "pass",           // password
                "SCSE",           // staffDepartment
                internshipService // pass the InternshipServiceImpl object here
        );

        // ------------------------------- Create Company Representative -------------------------------
        CompanyRep rep = new CompanyRep(
                "C001",
                "NTU Corp Rep",
                "pass",
                "NTU Corp",           // company
                "Technology",         // department
                "HR Manager"          // position
        );

        // ------------------------------- Create Internship Opportunities -------------------------------
        Internship i1 = new Internship(
                "AI Research Intern",
                "Assist in training AI models",
                InternshipLevel.BASIC,
                "SCSE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                "NTU Corp",
                3
        );

        Internship i2 = new Internship(
                "Robotics Intern",
                "Work on autonomous systems",
                InternshipLevel.ADVANCED,
                "EEE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(4),
                "NTU Corp",
                2
        );

        System.out.println("--- BEFORE Approval ---");
        printInternship(i1);
        printInternship(i2);

        // ------------------------------- Career Center Staff APPROVES i1 -------------------------------
        System.out.println("\n--- CCS Approves Internship 1 ---");
        staff.approveInternship(i1);
        printInternship(i1); // should be APPROVED + visible true

        // ------------------------------- Career Center Staff REJECTS i2 -------------------------------
        System.out.println("\n--- CCS Rejects Internship 2 ---");
        staff.rejectInternship(i2);
        printInternship(i2); // should be REJECTED + visible false

        System.out.println("\n===== END TEST CASE 21 =====");
    }

    private static void printInternship(Internship i) {
        System.out.println("Title: " + i.getTitle());
        System.out.println("Status: " + i.getStatus());
        System.out.println("Visible: " + i.isVisible());
        System.out.println("Major: " + i.getMajors());
        System.out.println("----------------------------------");
    }
}
