package control;

import java.time.LocalDate;
import java.util.List;

import boundary.CompanyRepView;
import entity.CompanyRep;
import entity.Internship;
import service.InternshipService;

public class CompanyRepController {
    private final CompanyRepView view;
    private final InternshipService service;

    public CompanyRepController(CompanyRepView view, InternshipService service) {
        this.view = view;
        this.service = service;
    }

    public void CreateInternship(CompanyRep rep) {
        if (!rep.isApproved()) {
            view.show("You are not approved by the Career Centre Staff.");
            return;
        }

        if (rep.getNumberOfInternships() >= 5) {
            view.show("Max 5 internships reached.");
            return;
        }

        // View handles all IO
        String title = view.promptNonEmpty("Title");
        String desc = view.promptNonEmpty("Description");
        String levelStr = view.promptLevel();
        String major = view.promptNonEmpty("Preferred Major");
        int slots = view.promptSlots();
        LocalDate open = view.promptDate("Open date");
        LocalDate close = view.promptDate("Close date");

        service.createInternship(rep, title, desc, levelStr, major, slots, open, close);
    }

    public void EditInternship(CompanyRep rep) {
        List<Internship> mine = service.getInternshipsFor(rep);
        mine = mine.stream()
                   .filter(i -> i.getCompany().equals(rep.getCompany()))
                   .toList();

        if (mine.isEmpty()) {
            view.show("No internships to edit.");
            return;
        }

        view.listInternships(mine,false);
        int idx = view.promptIndexSelection(mine.size());

        Internship target = mine.get(idx);
        service.editInternship(rep, target, view);
    }

    public void DeleteInternship(CompanyRep rep) {
        List<Internship> mine = service.getInternshipsFor(rep).stream()
                .filter(i -> i.getCompany().equals(rep.getCompany()))
                .toList();

        if (mine.isEmpty()) {
            view.show("No internships to delete.");
            return;
        }

        view.listInternships(mine,false);
        int idx = view.promptIndexSelection(mine.size());

        Internship target = mine.get(idx);
        service.deleteInternship(rep, target);
    }

    public void ToggleVisibility(CompanyRep rep) {
        List<Internship> mine = service.getInternshipsFor(rep).stream()
                .filter(i -> i.getCompany().equals(rep.getCompany()))
                .toList();

        if (mine.isEmpty()) {
            view.show("No internships to toggle.");
            return;
        }

        view.listInternships(mine,false);
        int idx = view.promptIndexSelection(mine.size());

        Internship target = mine.get(idx);

        boolean vis = view.promptVisibility();
        service.updateVisibility(target, vis);

        view.show("Visibility set to " + vis);
    }
    
    public void acceptApplication
    
    public void rejectApplication
}
