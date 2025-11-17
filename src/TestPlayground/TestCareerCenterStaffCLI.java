package TestPlayground;

import data.AccountCreationRepository;
import data.InternshipRepository;
import data.ApplicationRepository;
import boundary.CareerCenterStaffView;
import boundary.FilterView;
import control.CareerCenterStaffController;
import control.AccountCreationController;
import control.ApplicationController;
import control.FilterController;
import service.InternshipService;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Internship;
import entity.Student;
import entity.Application;
import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.ApplicationStatus;

public class TestCareerCenterStaffCLI {
    public static void main(String[] args) {
        AccountCreationRepository accRepo = new AccountCreationRepository();
        AccountCreationController accController = new AccountCreationController(accRepo);

        InternshipRepository internRepo = new InternshipRepository();
        InternshipService internService = new InternshipService(internRepo);

        ApplicationRepository appRepo = new ApplicationRepository();
        ApplicationController appController = new ApplicationController(appRepo);

        FilterView filterView = new FilterView();
        FilterController filterController = new FilterController(filterView, internService);

        CareerCenterStaffView staffView = new CareerCenterStaffView();
        CareerCenterStaffController staffController = new CareerCenterStaffController(staffView, accController, internService, appController, filterController);

        // seed a pending company rep
        CompanyRep rep = new CompanyRep("rep@example.com", "Rep", "pwd", "AcmeCorp", "HR", "Mgr");
        // not authorized by default
        accRepo.save(rep);

        // seed a pending internship
        Internship internship = new Internship("PendingIntern", "Desc", InternshipLevel.BASIC, "CSC", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(10), "AcmeCorp", 2);
        internRepo.add(internship);

        // seed a student and application with withdrawal requested
        Student student = new Student("s001", "Student", "pwd", 3, "CSC");
        Application application = new Application(student, internship, ApplicationStatus.PENDING);
        application.setWithdrawalRequested(true);
        appRepo.save(application);

        CareerCenterStaff staff = new CareerCenterStaff("staff1", "Staff", "pwd", "Careers");

        System.out.println("Starting Career Centre Staff interactive test.");
        staffController.showMain(staff);
        System.out.println("Exiting Career Centre Staff interactive test.");
    }
}
