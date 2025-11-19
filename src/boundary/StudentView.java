package boundary;

import entity.Internship;
import entity.Application;

import java.util.List;
import java.util.Scanner;

public class StudentView {
    private final Scanner sc = new Scanner(System.in);

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

    public boolean confirm(String question) {
        while (true) {
            System.out.print(question + " (y/n): ");
            String ans = sc.nextLine().trim();
            if (ans.equalsIgnoreCase("y")) return true;
            if (ans.equalsIgnoreCase("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }

    public void show(String msg) { System.out.println(msg); }
}
