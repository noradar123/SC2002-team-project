package boundary;

import java.util.List;

import entity.Internship;

/**
 * The view class responsible for the user interface interactions related to filtering internships.
 * It provides menus for selecting filter criteria (Status, Level, Major, etc.) and displays
 * the filtered results to the user.
 */
public class FilterView {
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    /**
     * Constructs a new FilterView.
     */
    public FilterView() {
        // Default constructor
    }

    /**
     * Displays the filter options menu and prompts the user for a selection.
     *
     * @return The integer option selected by the user (0-7).
     */
    public int promptFilterMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Filters ===");
            System.out.println("1) Add Status Filter (APPROVED/PENDING/REJECTED/FILLED)");
            System.out.println("2) Add Level Filter (BASIC/INTERMEDIATE/ADVANCED)");
            System.out.println("3) Add Major Filter");
            System.out.println("4) Only Visible");
            System.out.println("5) Only Currently Open");
            System.out.println("6) Add Year Filter (min year of student eligibility)");
            System.out.println("7) Clear all filters (reset to defaults)");
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
     * Prompts the user to enter an internship status string.
     *
     * @return The status string entered by the user.
     */
    public String promptStatus() {
        System.out.print("Status (APPROVED/PENDING/REJECTED/FILLED): ");
        return sc.nextLine().trim();
    }

    /**
     * Prompts the user to enter an internship level string.
     *
     * @return The level string entered by the user.
     */
    public String promptLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return sc.nextLine().trim();
    }

    /**
     * Prompts the user to enter a major.
     *
     * @return The major string entered by the user.
     */
    public String promptMajor() {
        System.out.print("Preferred Major: ");
        return sc.nextLine().trim();
    }

    /**
     * Prompts the user to enter a minimum student year (1-4).
     *
     * @return The valid integer year entered by the user.
     */
    public int promptMinYear() {
        while (true) {
            System.out.print("Minimum student year (1-4): ");
            try {
                int y = Integer.parseInt(sc.nextLine().trim());
                if (y >= 1 && y <= 4) return y;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid year.");
        }
    }

    /**
     * Displays a list of internships that match the current filters.
     * shows details such as Title, Level, Major, Slots, and Dates.
     *
     * @param list The list of filtered internships to display.
     */
    public void showInternships(List<Internship> list) {
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
     * Displays a generic message to the user.
     *
     * @param msg The message string.
     */
    public void show(String msg) { System.out.println(msg); }
}