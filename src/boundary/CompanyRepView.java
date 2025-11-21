package boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import entity.Internship;
import entity.Application;

/**
 * The view class responsible for user interactions specific to a Company Representative.
 * Handles input collection for creating/editing internships and displays internship/application lists.
 */
public class CompanyRepView {
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    /**
     * Constructs a new CompanyRepView.
     */
    public CompanyRepView() {
        // Default constructor
    }

    // ===================== Input Prompts =====================

    /**
     * Prompts the user for a non-empty string.
     * Repeats the prompt until valid input is received.
     *
     * @param label The label to display to the user.
     * @return The non-empty string entered by the user.
     */
    public String promptNonEmpty(String label) {
        while (true) {
            System.out.print(label + ": ");
            String v = sc.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("Input cannot be empty.");
        }
    }

    /**
     * Prompts the user for an optional string.
     *
     * @param label The label to display.
     * @return The string entered, or {@code null} if the user pressed Enter to skip.
     */
    public String promptOptionalString(String label) {
        System.out.print(label + " (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

    /**
     * Prompts the user to enter an internship level.
     *
     * @return The level string entered by the user.
     */
    public String promptLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return sc.nextLine().trim();
    }

    /**
     * Prompts the user for an optional internship level.
     *
     * @return The level string, or {@code null} if skipped.
     */
    public String promptOptionalLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED) (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

    /**
     * Prompts the user for a positive integer representing slot count.
     *
     * @return A positive integer.
     */
    public int promptSlots() {
        while (true) {
            System.out.print("Total Slots: ");
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n > 0) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a positive integer.");
        }
    }

    /**
     * Prompts the user to optionally set visibility.
     *
     * @return {@code true} for yes, {@code false} for no, or {@code null} to skip.
     */
    public Boolean promptOptionalVisibility() {
        System.out.print("Visible? (y/n) (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        if (v.isEmpty()) return null;
        return v.equalsIgnoreCase("y");
    }

    /**
     * Prompts the user to strictly set visibility.
     *
     * @return {@code true} for yes, {@code false} for no.
     */
    public boolean promptVisibility() {
        while (true) {
            System.out.print("Visible? (y/n): ");
            String v = sc.nextLine().trim();
            if (v.equalsIgnoreCase("y")) return true;
            if (v.equalsIgnoreCase("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }

    /**
     * Prompts the user for a date in dd/MM/yyyy format.
     *
     * @param label The label to display.
     * @return The parsed {@link LocalDate}.
     */
    public LocalDate promptDate(String label) {
        while (true) {
            System.out.print(label + " (dd/MM/yyyy): ");
            try {
                return LocalDate.parse(sc.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }

    /**
     * Prompts the user for an optional date.
     *
     * @param label The label to display.
     * @return The parsed {@link LocalDate}, or {@code null} if skipped or invalid.
     */
    public LocalDate promptOptionalDate(String label) {
        System.out.print(label + " (dd/MM/yyyy) (Press Enter to skip): ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s, fmt);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, skipped.");
            return null;
        }
    }

    // ===================== Display Methods =====================

    /**
     * Displays detailed information about a single internship.
     *
     * @param i The internship to display.
     */
    public void displayDetailed(Internship i) {
        System.out.printf("Title: %s%nDescription: %s%nLevel: %s%nMajor: %s%nSlots: %d/%d%nCompany: %s%nStatus: %s%nVisible: %b%nOpen: %s%nClose: %s%n%n",
                i.getTitle(), i.getDescription(), i.getLevel(), i.getPreferredMajor(),
                i.getFilledSlots(), i.getSlots(), i.getCompany(), i.getStatus(), i.isVisible(),
                i.getOpenDate(), i.getClosingDate());
    }

    /**
     * Displays a summary row for a single internship.
     *
     * @param i The internship to display.
     */
    public void displaySummary(Internship i) {
        System.out.printf("%s | %s | %s | %d/%d slots | %s%n",
                i.getTitle(), i.getLevel(), i.getPreferredMajor(),
                i.getFilledSlots(), i.getSlots(), i.getStatus());
    }

    /**
     * Lists a collection of internships with indices.
     *
     * @param list     The list of internships.
     * @param detailed If {@code true}, shows full details; otherwise shows summary.
     */
    public void listInternships(List<Internship> list, boolean detailed) {
        if (list.isEmpty()) {
            System.out.println("No internships found.");
            return;
        }
        for (int idx = 0; idx < list.size(); idx++) {
            System.out.printf("%d) ", idx + 1);
            if (detailed) displayDetailed(list.get(idx));
            else displaySummary(list.get(idx));
        }
    }

    /**
     * Lists a collection of internships in summary mode.
     *
     * @param list The list of internships.
     */
    public void listInternships(List<Internship> list) {
        listInternships(list, false);
    }

    /**
     * Prompts the user to select an index from a list.
     *
     * @param max The maximum valid index (1-based).
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
     * Displays the main menu options for a Company Representative and gets the user's choice.
     *
     * @return The integer option selected.
     */
    public int promptMainMenuOption() {
        while (true) {
            System.out.println();
            System.out.println("=== Company Representative Menu ===");
            System.out.println("1) Create Internship");
            System.out.println("2) Edit Internship");
            System.out.println("3) Delete Internship");
            System.out.println("4) Toggle Visibility");
            System.out.println("5) List My Internships (detailed)");
            System.out.println("6) Manage Applications");
            System.out.println("7) View internships & Manage filters");
            System.out.println("0) Back");
            System.out.print("Select option: ");
            String s = sc.nextLine().trim();
            try {
                int opt = Integer.parseInt(s);
                if (opt >= 0 && opt <= 7) return opt;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid option.");
        }
    }

    /**
     * Displays a summary of a single application.
     *
     * @param a The application to display.
     */
    public void displayApplicationSummary(Application a) {
        System.out.printf("%s | Student: %s | Status: %s | Date: %s | WithdrawalRequested: %b%n",
                a.getApplicationID(), a.getStudent().getUserId(), a.getStatus(), a.getApplicationDate(), a.isWithdrawalRequested());
    }

    /**
     * Lists a collection of applications with indices.
     *
     * @param list The list of applications.
     */
    public void listApplications(List<Application> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d) ", i + 1);
            displayApplicationSummary(list.get(i));
        }
    }

    /**
     * Prompts the user to choose between approving or rejecting an item.
     *
     * @return "approve" or "reject" based on user input.
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
     * Displays a generic message to the user.
     *
     * @param msg The message string.
     */
    public void show(String msg) { System.out.println(msg); }
}