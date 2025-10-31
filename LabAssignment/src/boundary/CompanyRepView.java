package boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import entity.Internship;

public class CompanyRepView {
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String promptNonEmpty(String label) { //General prompt method that is used by other prompts. Less code.
        while (true) {
            System.out.print(label + ": ");
            String v = sc.nextLine();
            if (!v.trim().isEmpty()) return v;
            System.out.println("Input cannot be empty.");
        }
    }

    
    public String promptOptionalString(String label) {
        System.out.print(label + " (Press Enter to skip this change): ");
        String v = sc.nextLine();
        return v.trim().isEmpty() ? null : v;
    }

    public String promptLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return sc.nextLine().trim();
    }

    public String promptOptionalLevel() {
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED) (Press Enter to skip this change): ");
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

    public int promptSlots() {
        while (true) {
            System.out.print("Total Slots: ");
            String s = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n > 0) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a positive integer.");
        }
    }

    public Boolean promptOptionalVisibility() {
        System.out.print("Visible? (y/n) (Press Enter to skip this change): ");
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
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    public LocalDate promptOptionalDate(String label) {
        System.out.print(label + " (dd/MM/yyyy) (Press Enter to skip this change): ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s, fmt);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Skipping change.");
            return null;
        }
    }

    public void listInternships(List<Internship> list) {
        System.out.println("Your internships:");
        int idx = 1;
        for (Internship i : list) {
            System.out.printf("%d) %s%n", idx++, i.toString());
        }
    }

    public int promptIndexSelection(int max) {
        while (true) {
            System.out.print("Select index: ");
            String s = sc.nextLine().trim();
            try {
                int sel = Integer.parseInt(s);
                if (sel >= 1 && sel <= max) return sel - 1;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid index selected.");
        }
    }

    public void show(String msg) { System.out.println(msg); }
}
