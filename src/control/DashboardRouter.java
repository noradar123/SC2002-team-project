package control;

import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Student;
import entity.User;

/**
 * A navigation utility that determines the appropriate dashboard to display based on the User type.
 * This acts as the central dispatch point after a successful login, routing the user to 
 * the specific interface corresponding to their role (Student, Company Rep, or Staff).
 */
public class DashboardRouter {

    /**
     * Constructs a new DashboardRouter.
     */
    public DashboardRouter() {
        // Default constructor
    }

    /**
     * Directs the user to their specific role-based dashboard.
     * Checks the instance type of the user and invokes the corresponding display logic.
     *
     * @param user The currently logged-in user.
     */
    public void route(User user) {
        if (user instanceof Student) {
            showStudentDashboard((Student) user);
        } else if (user instanceof CompanyRep) {
            showCompanyRepDashboard((CompanyRep) user);
        } else if (user instanceof CareerCenterStaff) {
            showStaffDashboard((CareerCenterStaff) user);
        } else {
            System.out.println("Unknown role. Contact administrator.");
        }
    }

    /**
     * Displays the dashboard for a Student user.
     * @param s The student user.
     */
    private void showStudentDashboard(Student s) {
        System.out.println("Welcome, " + s.getName() + " (Student).");
        System.out.println("- View eligible internships");
        System.out.println("- Apply / Withdraw / Accept");
    }

    /**
     * Displays the dashboard for a Company Representative user.
     * @param r The company representative user.
     */
    private void showCompanyRepDashboard(CompanyRep r) {
        System.out.println("Welcome, " + r.getName() + " (Company Representative).");
        System.out.println("- Create/Manage internships");
        System.out.println("- Review applications");
    }

    /**
     * Displays the dashboard for a Career Center Staff user.
     * @param c The staff user.
     */
    private void showStaffDashboard(CareerCenterStaff c) {
        System.out.println("Welcome, " + c.getName() + " (Career Center Staff).");
        System.out.println("- Approve company reps & internships");
        System.out.println("- Approve withdrawals");
        System.out.println("- Generate reports");
    }
}