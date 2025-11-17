package boundary;

import java.util.List;

import entity.Internship;
import entity.Application;
import enums.InternshipLevel;
import enums.InternshipStatus;

public class FilterView {
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

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

    public String promptStatus() {
        System.out.print("Status (APPROVED/PENDING/REJECTED/FILLED): ");
        return sc.nextLine().trim();
    }

    public String promptLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return sc.nextLine().trim();
    }

    public String promptMajor() {
        System.out.print("Preferred Major: ");
        return sc.nextLine().trim();
    }

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

    public void show(String msg) { System.out.println(msg); }
}
