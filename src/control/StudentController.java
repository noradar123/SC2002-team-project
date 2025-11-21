package control;

import boundary.StudentView;
import entity.Student;
import entity.Internship;
import entity.Application;
import enums.ApplicationStatus;
import service.InternshipService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The main controller for Student operations.
 * Handles the primary menu loop for students, orchestrating interactions between the Student View,
 * the Internship Service (for browsing), and the Application Controller (for applying/accepting).
 */
public class StudentController {
    private final StudentView view;
    private final InternshipService internshipService;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    /**
     * Constructs a new StudentController.
     *
     * @param view                  The UI view component for student interactions.
     * @param internshipService     The service used to retrieve eligible internships.
     * @param applicationController The controller used to handle application logic (create, withdraw, accept).
     * @param filterController      The controller used to manage the student's search filters.
     */
    public StudentController(StudentView view,
                             InternshipService internshipService,
                             ApplicationController applicationController,
                             FilterController filterController) {
        this.view = view;
        this.internshipService = internshipService;
        this.applicationController = applicationController;
        this.filterController = filterController;
    }

    /**
     * Displays the main menu and handles the primary navigation loop for a Student.
     * Keeps the session active until the student chooses to log out.
     *
     * @param student The currently logged-in Student.
     */
    public void showMain(Student student) {
        while (true) {
            int opt = view.promptMainMenu();
            switch (opt) {
                case 0: return;
                case 1: listInternships(student); break;
                case 2: applyToInternship(student); break;
                case 3: showApplications(student); break;
                case 4: requestWithdrawal(student); break;
                case 5: acceptPlacement(student); break;
                case 6: filterController.manageFiltersFor(student); break;
                default: view.show("Unknown option.");
            }
        }
    }

    /**
     * Retrieves and displays the list of internships that match the student's current filters.
     * @param student The student viewing the list.
     */
    private void listInternships(Student student) {
        List<Internship> internships = internshipService.getInternshipsFor(student);
        view.listInternships(internships);
    }

    /**
     * Handles the workflow for applying to a new internship.
     * Displays available internships, prompts for selection, and delegates creation to the ApplicationController.
     * @param student The student applying.
     */
    private void applyToInternship(Student student) {
        List<Internship> internships = internshipService.getInternshipsFor(student);
        if (internships.isEmpty()) {
            view.show("No internships available to apply.");
            return;
        }
        view.listInternships(internships);
        int idx = view.promptIndexSelection(internships.size());
        Internship chosen = internships.get(idx);
        try {
            Application app = applicationController.createApplication(student, chosen);
            view.show("Application submitted: " + app.getApplicationID());
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.show(e.getMessage());
        }
    }

    /**
     * Displays the history and status of all applications submitted by the student.
     * @param student The student viewing their history.
     */
    private void showApplications(Student student) {
        List<Application> apps = applicationController.getApplicationByStudentId(student.getUserId());
        view.listApplications(apps);
    }

    /**
     * Handles the workflow for requesting a withdrawal from an application.
     * Filters applications to show only those eligible for withdrawal (e.g., Pending).
     * @param student The student requesting withdrawal.
     */
    private void requestWithdrawal(Student student) {
        List<Application> withdrawable = applicationController.getApplicationByStudentId(student.getUserId())
                .stream()
                .filter(app -> app.canBeWithdrawn() && !app.isWithdrawalRequested())
                .collect(Collectors.toList());
        if (withdrawable.isEmpty()) {
            view.show("No applications can be withdrawn.");
            return;
        }
        view.listApplications(withdrawable);
        int idx = view.promptIndexSelection(withdrawable.size());
        Application target = withdrawable.get(idx);
        if (!view.confirm("Request withdrawal for " + target.getApplicationID() + "?")) return;
        try {
            applicationController.requestWithdrawal(target.getApplicationID());
            view.show("Withdrawal requested for " + target.getApplicationID());
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    /**
     * Handles the workflow for accepting a job offer.
     * Filters applications to show only those with SUCCESSFUL status.
     * @param student The student accepting the offer.
     */
    private void acceptPlacement(Student student) {
        List<Application> successful = applicationController.getApplicationByStudentId(student.getUserId())
                .stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL && !app.isWithdrawn())
                .collect(Collectors.toList());
        if (successful.isEmpty()) {
            view.show("You have no successful applications to accept.");
            return;
        }
        view.listApplications(successful);
        int idx = view.promptIndexSelection(successful.size());
        Application target = successful.get(idx);
        if (!view.confirm("Accept placement for " + target.getApplicationID() + "?")) return;
        try {
            applicationController.acceptPlacement(target.getApplicationID());
            view.show("Placement accepted. Other active applications withdrawn.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }
}