package TestPlayground;

import boundary.CompanyRepView;
import boundary.FilterView;
import control.CompanyRepController;
import control.ApplicationController;
import control.FilterController;
import data.InternshipRepository;
import data.ApplicationRepository;
import entity.CompanyRep;
import entity.Student;
import entity.Internship;
import service.InternshipService;

import enums.InternshipLevel;
import enums.InternshipStatus;

public class TestCompanyRepCLI {
    public static void main(String[] args) {
        InternshipRepository repo = new InternshipRepository();
        InternshipService service = new InternshipService(repo);
        ApplicationRepository appRepo = new ApplicationRepository();
        ApplicationController appController = new ApplicationController(appRepo);

        CompanyRepView view = new CompanyRepView();
        FilterView filterView = new FilterView();
        FilterController filterController = new FilterController(filterView, service);
        CompanyRepController controller = new CompanyRepController(view, service, appController, filterController);

        // Create a sample company rep and approve them so they can create internships
        CompanyRep rep = new CompanyRep("rep1", "Alice", "password", "AcmeCorp", "HR", "Manager");
        rep.approveByCCS();

        // Create a sample internship (use service to ensure counters updated)
        Internship internship = service.createInternship(rep, "Intern Dev", "Work on projects", InternshipLevel.BASIC, "CSC", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(30), 2);
        // mark internship as approved so students can apply in this test harness
        internship.setStatus(InternshipStatus.APPROVED);

        // Create a sample student and application so rep can manage it
        Student student = new Student("s001", "Bob", "pass", 3, "CSC");
        appController.createApplication(student, internship);

        System.out.println("Starting CompanyRep interactive test. Use the menu to create/edit/delete internships and manage applications.");
        controller.showMain(rep);

        System.out.println("Exiting CompanyRep interactive test.");
    }
}