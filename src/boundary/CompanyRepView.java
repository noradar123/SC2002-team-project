package boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import entity.Internship;

public class CompanyRepView {
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    // Input prompts
    public String promptNonEmpty(String label) {
        while (true) {
            System.out.print(label + ": ");
            String v = sc.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("Input cannot be empty.");
        }
    }

    public String promptOptionalString(String label) {
        System.out.print(label + " (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

    public String promptLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return sc.nextLine().trim();
    }

    public String promptOptionalLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED) (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

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

    public Boolean promptOptionalVisibility() {
        System.out.print("Visible? (y/n) (Press Enter to skip): ");
        String v = sc.nextLine().trim();
        if (v.isEmpty()) return null;
        return v.equalsIgnoreCase("y");
    }

    public boolean promptVisibility() {
        while (true) {
            System.out.print("Visible? (y/n): ");
            String v = sc.nextLine().trim();
            if (v.equalsIgnoreCase("y")) return true;
            if (v.equalsIgnoreCase("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }

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

    // Display internships
    public void displayDetailed(Internship i) {
        System.out.printf("Title: %s%nDescription: %s%nLevel: %s%nMajor: %s%nSlots: %d/%d%nCompany: %s%nStatus: %s%nVisible: %b%nOpen: %s%nClose: %s%n%n",
                i.getTitle(), i.getDescription(), i.getLevel(), i.getPreferredMajor(),
                i.getFilledSlots(), i.getSlots(), i.getCompany(), i.getStatus(), i.isVisible(),
                i.getOpenDate(), i.getClosingDate());
    }

    public void displaySummary(Internship i) {
        System.out.printf("%s | %s | %s | %d/%d slots | %s%n",
                i.getTitle(), i.getLevel(), i.getPreferredMajor(),
                i.getFilledSlots(), i.getSlots(), i.getStatus());
    }

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

    public void show(String msg) { System.out.println(msg); }
}
