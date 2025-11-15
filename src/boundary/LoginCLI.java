package boundary;

import java.util.Scanner;

import control.AuthService;
import control.AuthService.AuthException;
import control.DashboardRouter;
import data.InMemoryUserRepository;
import data.UserRepository;
import entity.User;

public class LoginCLI {

    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();
        AuthService auth = new AuthService(repo);
        DashboardRouter router = new DashboardRouter();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n== IPMS Login ==");
                System.out.println("1) Login");
                System.out.println("2) Change Password");
                System.out.println("3) Exit");
                System.out.print("Choose: ");
                String choice = sc.nextLine().trim();

                if ("1".equals(choice)) {
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    System.out.print("Password: ");
                    String pw = sc.nextLine();

                    try {
                        User u = auth.authenticate(id, pw);
                        System.out.println("Login successful.");
                        router.route(u); // Test Case 1: send to correct dashboard
                    } catch (AuthException e) {
                        System.out.println("Login failed: " + e.getMessage()); // TC2/TC3 messages
                    }

                } else if ("2".equals(choice)) {
                    System.out.println("== Change Password ==");
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    System.out.print("Current password: ");
                    String oldPw = sc.nextLine();
                    System.out.print("New password: ");
                    String newPw = sc.nextLine();

                    try {
                        auth.changePassword(id, oldPw, newPw);
                        System.out.println("Password updated. Please log in again with your new password."); // TC4
                    } catch (AuthException e) {
                        System.out.println("Change password failed: " + e.getMessage());
                    }

                } else if ("3".equals(choice)) {
                    System.out.println("Goodbye.");
                    return;
                } else {
                    System.out.println("Invalid option.");
                }
            }
        }
    }
}
