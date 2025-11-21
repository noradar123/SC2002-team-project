package boundary;

import entity.Internship;
import entity.Application;

import java.util.List;
import java.util.Scanner;

/**
 * The view class responsible for user interactions specific to a Student.
 * Handles the display of internship listings, application history, and captures 
 * user input for the main student menu options.
 */
public class StudentView {
    
    private final Scanner sc = new Scanner(System.in);

    /**
     * Constructs a new StudentView.
     */
    public StudentView() {
        // Default constructor
    }

    /**
     * Displays the main menu options available to a Student and captures their choice.
     *
     * @return The integer option selected by the user (0-6).
     */
    public int promptMainMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Student Menu ===");
            System.out.println("1) View available internships");
            System.out.println("2) Apply to an internship");
            System.out.println("3) View my applications");
            System.out.println("4) Request withdrawal");
            System.out.println("5) Accept placement");
            System.out.println("6) Manage filters");
            System.out.println("0) Back");
            System.out.print("Select option: ");
            try {
                int opt = Integer.parseInt(sc.nextLine().trim());
                if (opt >= 0 && opt <= 6) return opt;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid option.");
        }
    }

    /**
     * Displays a formatted list of internships to the console.
     * Shows details such as Title, Level, Company, Slots, and Status.
     *
     * @param internships The list of internships to display.
     */
    public void listInternships(List<Internship> internships) {
        if (internships == null || internships.isEmpty()) {
            System.out.println("No internships matched your filters.");
            return;
        }
        for (int i = 0; i < internships.size(); i++) {
            Internship in = internships.get(i);
            System.out.printf("%d) %s | %s | %s | Company: %s | %d/%d slots | Status:%s | Open:%s Close:%s%n",
                    i + 1,
                    in.getTitle(),
                    in.getLevel(),
                    in.getPreferredMajor(),
                    in.getCompany(),
                    in.getFilledSlots(),
                    in.getSlots(),
                    in.getStatus(),
                    in.getOpenDate(),
                    in.getClosingDate());
        }
    }

    /**
     * Displays a formatted list of the student's existing applications.
     * Shows details including Application ID, Internship Title, and Withdrawal status.
     *
     * @param apps The list of applications to display.
     */
    public void listApplications(List<Application> apps) {
        if (apps == null || apps.isEmpty()) {
            System.out.println("You have no applications yet.");
            return;
        }
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.printf("%d) %s | %s | Status:%s | WithdrawalRequested:%b | Withdrawn:%b%n",
                    i + 1,
                    app.getApplicationID(),
                    app.getInternship().getTitle(),
                    app.getStatus(),
                    app.isWithdrawalRequested(),
                    app.isWithdrawn());
        }
    }

    /**
     * Prompts the user to select an item from a list by index.
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
     * Prompts the user for a Yes/No confirmation.
     *
     * @param question The question or prompt to display.
     * @return {@code true} if user enters 'y', {@code false} if 'n'.
     */
    public boolean confirm(String question) {
        while (true) {
            System.out.print(question + " (y/n): ");
            String ans = sc.nextLine().trim();
            if (ans.equalsIgnoreCase("y")) return true;
            if (ans.equalsIgnoreCase("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }

    /**
     * Displays a generic message string to the console.
     *
     * @param msg The message to display.
     */
    public void show(String msg) { System.out.println(msg); }
}