package boundary;

import java.util.List;

import entity.CompanyRep;
import entity.Internship;
import entity.Application;

public class CareerCenterStaffView {
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

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

    public String promptApproveOrReject() {
        while (true) {
            System.out.print("Approve or Reject? (a/r): ");
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("a")) return "approve";
            if (s.equalsIgnoreCase("r")) return "reject";
            System.out.println("Invalid choice. Enter 'a' to approve or 'r' to reject.");
        }
    }

    public void show(String msg) { System.out.println(msg); }
}
