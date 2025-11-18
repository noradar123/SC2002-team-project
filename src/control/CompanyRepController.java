package control;

import java.time.LocalDate;
import java.util.List;

import boundary.CompanyRepView;
import entity.CompanyRep;
import entity.Internship;
import entity.Application;
import enums.InternshipLevel;
import enums.InternshipStatus;
import service.InternshipService;

public class CompanyRepController {

    private final CompanyRepView view;
    private final InternshipService internshipService;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    public CompanyRepController(CompanyRepView view, InternshipService internshipService, ApplicationController applicationController, FilterController filterController) {
        this.view = view;
        this.internshipService = internshipService;
        this.applicationController = applicationController;
        this.filterController = filterController;
    }

    // Main menu loop for CompanyRep
    public void showMain(CompanyRep rep) {
        while (true) {
            int opt = view.promptMainMenuOption();
            switch (opt) {
                case 0:
                    return;
                case 1:
                    createInternship(rep);
                    break;
                case 2:
                    editInternship(rep);
                    break;
                case 3:
                    deleteInternship(rep);
                    break;
                case 4:
                    toggleVisibility(rep);
                    break;
                case 5:
                    listMyInternships(rep);
                    break;
                case 6:
                    manageApplications(rep);
                    break;
                case 7:
                    filterController.manageFiltersFor(rep);
                    break;
                default:
                    view.show("Unknown option.");
            }
        }
    }

    // Manage applications for an internship (approve/reject)
    public void manageApplications(CompanyRep rep) {
        List<Internship> mine = internshipService.getInternshipsFor(rep.getCompany());
        if (mine.isEmpty()) {
            view.show("No internships to manage applications for.");
            return;
        }

        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship selected = mine.get(idx);

        // fetch applications for the internship
        List<Application> apps = applicationController.getApplicationByInternshipTitle(selected.getTitle());
        if (apps.isEmpty()) {
            view.show("No applications for this internship.");
            return;
        }

        view.listApplications(apps);
        int aidx = view.promptIndexSelection(apps.size());
        Application chosen = apps.get(aidx);

        String action = view.promptApproveOrReject();
        try {
            if (action.equals("approve")) {
                applicationController.approveApplication(chosen.getApplicationID());
                view.show("Application approved: " + chosen.getApplicationID());
            } else {
                applicationController.rejectApplication(chosen.getApplicationID());
                view.show("Application rejected: " + chosen.getApplicationID());
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // Create a new internship
    public void createInternship(CompanyRep rep) {
        if (!rep.isAuthorized()) {
            view.show("You are not approved by the Career Centre Staff.");
            return;
        }
        if (rep.getNumberOfInternships() >= 5) {
            view.show("Max 5 internships reached.");
            return;
        }

        String title = view.promptNonEmpty("Title");
        String desc = view.promptNonEmpty("Description");
        String levelStr = view.promptLevel();
        String major = view.promptNonEmpty("Preferred Major");
        int slots = view.promptSlots();
        LocalDate open = view.promptDate("Open date");
        LocalDate close = view.promptDate("Close date");

        InternshipLevel level;
        try {
            level = InternshipLevel.valueOf(levelStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            view.show("Invalid level.");
            return;
        }

        try {
            Internship internship = internshipService.createInternship(
                    rep, title, desc, level, major, open, close, slots
            );
            view.show("Created: " + internship.getTitle());
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // Edit an internship
    public void editInternship(CompanyRep rep) {
        List<Internship> mine = internshipService.getInternshipsFor(rep);
        if (mine.isEmpty()) {
            view.show("No internships to edit.");
            return;
        }

        view.listInternships(mine,false);
        int idx = view.promptIndexSelection(mine.size());
        Internship selected = mine.get(idx);

        if (selected.getStatus() != InternshipStatus.PENDING) {
            view.show("Only pending internships can be edited.");
            return;
        }

        // Prompt optional fields
        String title = view.promptOptionalString("Title");
        String desc = view.promptOptionalString("Description");
        String major = view.promptOptionalString("Preferred Major");
        String levelStr = view.promptOptionalLevel();
        LocalDate open = view.promptOptionalDate("Open date");
        LocalDate close = view.promptOptionalDate("Close date");
        Boolean vis = view.promptOptionalVisibility();

        try {
            Internship updated = internshipService.editInternship(selected.getId(), title, desc, major, levelStr, open, close, vis);
            view.show("Edited: " + updated.getTitle());
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // Delete an internship
    public void deleteInternship(CompanyRep rep) {
        List<Internship> mine = internshipService.getInternshipsFor(rep.getCompany());
        if (mine.isEmpty()) {
            view.show("No internships to delete.");
            return;
        }

        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship selected = mine.get(idx);

        try {
            internshipService.deleteInternship(rep, selected.getId());
            view.show("Deleted: " + selected.getTitle());
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // Toggle visibility
    public void toggleVisibility(CompanyRep rep) {
        List<Internship> mine = internshipService.getInternshipsFor(rep.getCompany());
        if (mine.isEmpty()) {
            view.show("No internships to toggle visibility.");
            return;
        }

        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship selected = mine.get(idx);

        boolean vis = view.promptVisibility();
        internshipService.setVisibility(selected.getId(), vis);
        view.show("Visibility set to " + vis + ".");
    }

    // List detailed internships for rep
    public void listMyInternships(CompanyRep rep) {
        List<Internship> mine = internshipService.getInternshipsFor(rep.getCompany());
        view.listInternships(mine, true);
    }
}