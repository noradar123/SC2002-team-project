package boundary;

import java.util.List;

import entity.CompanyRep;
import entity.Internship;
import entity.Application;

/**
 * View class for Career Centre Staff interactions.
 * Handles the display of the staff menu and processes menu selections
 * for authorizing representatives, approving internships, and managing requests.
 */
public class CareerCenterStaffView {
    
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    /**
     * Constructs a new CareerCenterStaffView.
     */
    public CareerCenterStaffView() {
        // Default constructor
    }

    /**
     * Displays the main menu and prompts the user for a selection.
     * Validates input and returns a valid option (0-4).
     *
     * @return The selected menu option as an integer.
     */
    public int promptMainMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Career Centre Staff Menu ===");
            System.out.println("1) Authorise / Reject Company Representatives");
            System.out.println("2) Approve / Reject Internship opportunities");
            System.out.println("3) Approve / Reject Withdrawal requests");
            System.out.println("4) View internships & Manage filters");
            System.out.println("0) Back");
            System.out.print("Select option: ");
            String s = sc.nextLine().trim();
            try {
                int opt = Integer.parseInt(s);
                if (opt >= 0 && opt <= 4) return opt;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid option.");
        }
    }

    /**
     * Displays a formatted list of Company Representatives.
     * Shows details such as User ID, Company, Department, and Authorization status.
     *
     * @param reps The list of company representatives to display.
     */
    public void listCompanyReps(List<CompanyRep> reps) {
        if (reps == null || reps.isEmpty()) {
            System.out.println("No pending company representative accounts.");
            return;
        }
        for (int i = 0; i < reps.size(); i++) {
            CompanyRep r = reps.get(i);
            System.out.printf("%d) %s | Company: %s | Dept: %s | Position: %s | Authorized: %b\n",
                    i+1, r.getUserId(), r.getCompany(), r.getDepartment(), r.getPosition(), r.isAuthorized());
        }
    }

    /**
     * Displays a formatted list of Internships.
     * Shows details such as Title, Level, Slots, and Visibility status.
     *
     * @param list The list of internships to display.
     */
    public void listInternships(List<Internship> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No internships.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Internship in = list.get(i);
            System.out.printf("%d) %s | %s | %s | %d/%d slots | %s | Visible:%b | Open:%s Close:%s\n",
                    i+1, in.getTitle(), in.getLevel(), in.getPreferredMajor(), in.getFilledSlots(), in.getSlots(), in.getStatus(), in.isVisible(), in.getOpenDate(), in.getClosingDate());
        }
    }

    /**
     * Displays a formatted list of Applications that have requested withdrawal.
     * Shows details such as Application ID, Student ID, and Internship Title.
     *
     * @param apps The list of applications to display.
     */
    public void listWithdrawalRequests(List<Application> apps) {
        if (apps == null || apps.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        for (int i = 0; i < apps.size(); i++) {
            Application a = apps.get(i);
            System.out.printf("%d) %s | Student: %s | Internship: %s | Status: %s | WithdrawalRequested: %b\n",
                    i+1, a.getApplicationID(), a.getStudent().getUserId(), a.getInternship().getTitle(), a.getStatus(), a.isWithdrawalRequested());
        }
    }

    /**
     * Prompts the user to select an index from a list.
     * Input is validated to ensure it is a number within the valid range (1 to max).
     *
     * @param max The maximum valid index (size of the list).
     * @return The 0-based index selected by the user.
     */
    public int promptIndexSelection(int max) {
        while (true) {
            System.out.print("Select index: ");
            try {
                int sel = Integer.parseInt(sc.nextLine().trim());
                if (sel >= 1 && sel <= max) return sel - 1;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid index.");
        }
    }

    /**
     * Prompts the user to make an Approve or Reject decision.
     *
     * @return "approve" if user enters 'a', or "reject" if user enters 'r'.
     */
    public String promptApproveOrReject() {
        while (true) {
            System.out.print("Approve or Reject? (a/r): ");
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("a")) return "approve";
            if (s.equalsIgnoreCase("r")) return "reject";
            System.out.println("Invalid choice. Enter 'a' to approve or 'r' to reject.");
        }
    }

    /**
     * Displays a generic message string to the console.
     *
     * @param msg The message to display.
     */
    public void show(String msg) { System.out.println(msg); }
}