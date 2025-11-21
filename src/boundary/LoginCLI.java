package boundary;

import java.util.Scanner;

import control.AuthService;
import control.AuthService.AuthException;
import control.AccountCreationController;
import control.ApplicationController;
import control.CareerCenterStaffController;
import control.CompanyRepController;
import control.FilterController;
import control.StudentController;

import data.UserLookupRepository;
import data.UserWriteRepository;
import data.InMemoryUserRepository;
import data.InternshipRepository;
import data.ApplicationRepository;
import data.AccountCreationRepository;
import data.AccountCreationUserRepositoryAdapter;
import data.CsvAccountBootstrapper;

import service.InternshipService;

import entity.Student;
import entity.User;
import entity.CompanyRep;
import entity.CareerCenterStaff;

/**
 * The main entry point for the Internship Placement Management System (IPMS).
 * This class bootstraps the application by initializing repositories, services,
 * controllers, and views, and then launches the main command-line interface loop.
 */
public class LoginCLI {

    /**
     * Constructs a new LoginCLI.
     * (Standard default constructor).
     */
    public LoginCLI() {
        // Default constructor
    }

    /**
     * The main method that launches the application.
     * It performs the following initialization steps:
     * <ol>
     * <li>Sets up data repositories and loads initial data from CSV files.</li>
     * <li>Initializes business logic services.</li>
     * <li>Initializes UI views and controllers, performing dependency injection.</li>
     * <li>Starts the main menu loop for Login, Registration, and Password Management.</li>
     * </ol>
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        // ---------- Repository Setup ----------

        AccountCreationRepository accountCreationRepo = new AccountCreationRepository();
        AccountCreationUserRepositoryAdapter userRepo =
                new AccountCreationUserRepositoryAdapter(accountCreationRepo);

        // Load CSV users
        CsvAccountBootstrapper.loadAllUsers(
                accountCreationRepo,
                "src/data/sample_student_list.csv",
                "src/data/sample_staff_list.csv",
                "src/data/sample_company_representative_list.csv"
        );

        ApplicationRepository applicationRepo = new ApplicationRepository();
        InternshipRepository internshipRepo = new InternshipRepository();

        // ---------- Services ----------

        AuthService authService = new AuthService(userRepo, userRepo);
        InternshipService internshipService = new InternshipService(internshipRepo);

        // ---------- Views ----------

        CompanyRepView companyRepView = new CompanyRepView();
        CareerCenterStaffView staffView = new CareerCenterStaffView();
        FilterView filterView = new FilterView();
        StudentView studentView = new StudentView();
        CompanyRepAccountCreationView repAccountView;

        // ---------- Controllers ----------

        AccountCreationController accountCreationController =
                new AccountCreationController(accountCreationRepo);

        ApplicationController applicationController =
                new ApplicationController(applicationRepo, internshipRepo, userRepo);

        FilterController filterController =
                new FilterController(filterView, internshipService);

        CompanyRepController companyRepController =
                new CompanyRepController(
                        companyRepView,
                        internshipService,
                        applicationController,
                        filterController
                );

        CareerCenterStaffController staffController =
                new CareerCenterStaffController(
                        staffView,
                        accountCreationController,
                        internshipService,
                        applicationController,
                        filterController
                );

        StudentController studentController = new StudentController(
                studentView,
                internshipService,
                applicationController,
                filterController
        );

        repAccountView = new CompanyRepAccountCreationView(accountCreationController);

        // ---------- Main CLI Loop ----------

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("=== Internship Placement Management System ===");
                System.out.println("1) Login");
                System.out.println("2) Register as Company Representative");
                System.out.println("3) Change Password");
                System.out.println("4) Exit");
                System.out.print("Choice: ");

                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        handleLogin(sc, authService, companyRepController, staffController, studentController);
                        break;

                    case "2":
                        repAccountView.createAccount();
                        break;

                    case "3":
                        handleChangePassword(sc, authService);
                        break;

                    case "4":
                        System.out.println("Goodbye.");
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }

    /**
     * Handles the login workflow.
     * Prompts for credentials, attempts authentication, and routes the user
     * to the appropriate dashboard based on their role (Student, Staff, or Company Rep).
     *
     * @param sc                   The scanner for input.
     * @param auth                 The authentication service.
     * @param companyRepController Controller for Company Rep dashboard.
     * @param staffController      Controller for Staff dashboard.
     * @param studentController    Controller for Student dashboard.
     */
    private static void handleLogin(
            Scanner sc,
            AuthService auth,
            CompanyRepController companyRepController,
            CareerCenterStaffController staffController,
            StudentController studentController) {

        System.out.print("User ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        try {
            User u = auth.authenticate(id, pw);
            System.out.println("Login successful.\n");

            if (u instanceof CompanyRep) {
                companyRepController.showMain((CompanyRep) u);
            } else if (u instanceof CareerCenterStaff) {
                staffController.showMain((CareerCenterStaff) u);
            } else if (u instanceof Student) {
                studentController.showMain((Student) u);
            } else {
                System.out.println("Dashboard for this role is not implemented yet.");
            }

        } catch (AuthException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    /**
     * Handles the password change workflow.
     * Prompts for User ID, old password, and new password, then delegates to the AuthService.
     *
     * @param sc   The scanner for input.
     * @param auth The authentication service.
     */
    private static void handleChangePassword(Scanner sc, AuthService auth) {
        System.out.print("User ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Current password: ");
        String oldPwd = sc.nextLine();
        System.out.print("New password: ");
        String newPwd = sc.nextLine();

        try {
            auth.changePassword(id, oldPwd, newPwd);
            System.out.println("Password updated. Please log in again with your new password.");
        } catch (AuthException e) {
            System.out.println("Change password failed: " + e.getMessage());
        }
    }
}