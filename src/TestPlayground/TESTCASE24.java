package TestPlayground;

import entity.*;
import enums.InternshipLevel;
import enums.InternshipStatus;
import service.InternshipServiceImpl;
import store.InternshipRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TESTCASE24 {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println("===== TEST CASE 24: Generate & Filter Internship Opportunities =====");

        // Create Repository FIRST
        InternshipRepository repo = new InternshipRepository();
        InternshipServiceImpl internshipService = new InternshipServiceImpl(repo);

        // Create Career Center Staff with the service
        CareerCenterStaff staff = new CareerCenterStaff(
                "S001",
                "Admin",
                "pass",
                "SCSE",
                internshipService
        );

        // Create Internship Opportunities
        Internship i1 = new Internship(
                "AI Research Intern",
                "Work on AI models",
                InternshipLevel.BASIC,
                "SCSE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                "NTU Corp",
                3
        );

        Internship i2 = new Internship(
                "Robotics Intern",
                "Work on robotics systems",
                InternshipLevel.ADVANCED,
                "EEE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(4),
                "NTU Corp",
                2
        );

        Internship i3 = new Internship(
                "Web Development Intern",
                "Develop websites",
                InternshipLevel.BASIC,
                "SCSE",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(6),
                "TechCo",
                4
        );

        //  CRITICAL: Save internships to repository
        repo.save(i1);
        repo.save(i2);
        repo.save(i3);

        // -------------------------------
        // BEFORE Approval - Print Internships
        // -------------------------------
        System.out.println("\n--- BEFORE Approval ---");
        printInternship(i1);
        printInternship(i2);
        printInternship(i3);

        // -------------------------------
        // CCS Approves i1
        // -------------------------------
        System.out.println("\n--- CCS Approves Internship 1 ---");
        staff.approveInternship(i1);
        repo.update(i1); // Update in repository too
        printInternship(i1);

        // -------------------------------
        // CCS Rejects i2
        // -------------------------------
        System.out.println("\n--- CCS Rejects Internship 2 ---");
        staff.rejectInternship(i2);
        repo.update(i2); // Update in repository too!
        printInternship(i2);

        // -------------------------------
        // FILTERING Demonstration:
        // -------------------------------

        System.out.println("\n--- Filter by Status: APPROVED ---");
        List<Internship> approvedInternships = staff.filterInternships("APPROVED", null, null);
        for (Internship i : approvedInternships) {
            printInternship(i);
        }

        System.out.println("\n--- Filter by Major: SCSE ---");
        List<Internship> scseInternships = staff.filterInternships(null, null, "SCSE");
        for (Internship i : scseInternships) {
            printInternship(i);
        }

        System.out.println("\n--- Filter by Level: BASIC ---");
        List<Internship> basicInternships = staff.filterInternships(null, "BASIC", null);
        for (Internship i : basicInternships) {
            printInternship(i);
        }

        System.out.println("\n--- Filter by Company: NTU Corp ---");
        // For company filter, use the repository method directly
        List<Internship> ntuInternships = repo.findByCompany("NTU Corp");
        for (Internship i : ntuInternships) {
            printInternship(i);
        }

        // -------------------------------
        // REPORT Generation - USING YOUR METHODS:
        // -------------------------------
        System.out.println("\n--- Generating Report ---");
        List<Internship> allInternships = repo.findAll();
        Report report = staff.generateReport(allInternships);

        // Use your displaySummary() method
        report.displaySummary();

        //  Show statistics
        System.out.println("\n--- Detailed Statistics ---");
        Map<String, Object> stats = report.getStatistics();
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        //  Show all opportunities in the report
        System.out.println("\n--- All Opportunities in Report ---");
        for (Internship i : report.getIncludedOpportunities()) {
            System.out.println("• " + i.getTitle() +
                    " | Status: " + i.getStatus() +
                    " | Level: " + i.getLevel() +
                    " | Major: " + i.getMajors() +
                    " | Company: " + i.getCompany());
        }

        //  Optional: Export to file
        System.out.println("\n--- Exporting Report to File ---");
        boolean exportSuccess = report.exportToFile("internship_report.txt");
        if (exportSuccess) {
            System.out.println("Report successfully exported to 'internship_report.txt'");
        } else {
            System.out.println(" Failed to export report");
        }

        System.out.println("\n===== END TEST CASE 24 =====");
    }

    private static void printInternship(Internship i) {
        System.out.println("Title: " + i.getTitle());
        System.out.println("Status: " + i.getStatus());
        System.out.println("Visible: " + i.isVisible());
        System.out.println("Major: " + i.getMajors());
        System.out.println("Level: " + i.getLevel());
        System.out.println("Company: " + i.getCompany());
        System.out.println("----------------------------------");
    }
}