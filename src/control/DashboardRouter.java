package control;

import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Student;
import entity.User;

public class DashboardRouter {

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

    private void showStudentDashboard(Student s) {
        System.out.println("Welcome, " + s.getName() + " (Student).");
        System.out.println("- View eligible internships");
        System.out.println("- Apply / Withdraw / Accept");
    }

    private void showCompanyRepDashboard(CompanyRep r) {
        System.out.println("Welcome, " + r.getName() + " (Company Representative).");
        System.out.println("- Create/Manage internships");
        System.out.println("- Review applications");
    }

    private void showStaffDashboard(CareerCenterStaff c) {
        System.out.println("Welcome, " + c.getName() + " (Career Center Staff).");
        System.out.println("- Approve company reps & internships");
        System.out.println("- Approve withdrawals");
        System.out.println("- Generate reports");
    }
}
