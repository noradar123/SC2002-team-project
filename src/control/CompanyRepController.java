package control;

import boundary.CompanyRepView;
import data.InternshipRepository;
import entity.CompanyRep;
import entity.Internship;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.time.LocalDate;
import java.util.List;

public class CompanyRepController {
    private final CompanyRepView view;
    private final InternshipRepository repo;

    public CompanyRepController(CompanyRepView view, InternshipRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    public void CreateInternship(CompanyRep rep) {
        if (!rep.isApproved()) { view.show("Your are not approved by the Career Centre Staff."); return; }
        if (rep.getNumberOfInternships() >= 5) { view.show("Max 5 internships reached."); return; }

        String title = view.promptNonEmpty("Title");
        String desc = view.promptNonEmpty("Description");
        String levelStr = view.promptLevel();
        String major = view.promptNonEmpty("Preferred Major");
        int slots = view.promptSlots();
        LocalDate open = view.promptDate("Open date");
        LocalDate close = view.promptDate("Close date");

        InternshipLevel level;
        try { level = InternshipLevel.valueOf(levelStr.toUpperCase()); }
        catch (IllegalArgumentException e) { view.show("Invalid level."); return; }

        if (!close.isAfter(open)) { view.show("Close date must be after open date."); return; }

        Internship i = new Internship(title, desc, level, major, open, close, rep.getCompany(), slots);
        repo.add(i);
        rep.incrementInternships();
        view.show("Created: " + i.toString());
    }

    public void EditInternship(CompanyRep rep) {
        List<Internship> mine = repo.findByCompany(rep.getCompany());
        if (mine.isEmpty()) { view.show("No internships to edit."); return; }
        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship i = mine.get(idx);

        if (i.getStatus() != InternshipStatus.PENDING) { view.show("Only pending internship can be edited."); return; }

        String title = view.promptOptionalString("Title");
        String desc = view.promptOptionalString("Description");
        String major = view.promptOptionalString("Preferred Major");
        String levelStr = view.promptOptionalLevel();
        LocalDate open = view.promptOptionalDate("Open date");
        LocalDate close = view.promptOptionalDate("Close date");
        Boolean vis = view.promptOptionalVisibility();
        
        //because null is return by CompanyRepView to check if certain changes are skip
        if (title != null) i.setTitle(title);
        if (desc != null) i.setDescription(desc);
        if (major != null) i.setMajor(major);
        if (levelStr != null) {
            try { i.setLevel(InternshipLevel.valueOf(levelStr.toUpperCase())); }
            catch (IllegalArgumentException e) { view.show("Invalid level. Skipped level update."); }
        }
        if (open != null) i.setOpenDate(open);
        if (close != null) i.setCloseDate(close);
        if (vis != null) i.setVisibility(vis);

        view.show("Edited: " + i.toString());
    }

    public void DeleteInternship(CompanyRep rep) {
        List<Internship> mine = repo.findByCompany(rep.getCompany());
        if (mine.isEmpty()) { view.show("No internships to delete."); return; }
        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship i = mine.get(idx);
        
        //This way, once approved, and students are planning or have applied, company rep cannot be scummy and delete.
        if (!(i.getStatus() == InternshipStatus.PENDING || i.getStatus() == InternshipStatus.REJECTED)) {
            view.show("Only pending or rejected internships can be deleted.");
            return;
        }

        if (repo.remove(i)) {
            rep.decrementInternships();
            view.show(String.format("%s deleted.", i.toString()));
        } else {
            view.show("Fail to delete.");
        }
    }

    public void ToggleVisibility(CompanyRep rep) {
        List<Internship> mine = repo.findByCompany(rep.getCompany());
        if (mine.isEmpty()) { view.show("No internships to toggle its visibility."); return; }
        view.listInternships(mine);
        int idx = view.promptIndexSelection(mine.size());
        Internship i = mine.get(idx);

        boolean vis = view.promptVisibility();
        i.setVisibility(vis);
        view.show("Visibility set to " + vis + ".");
    }
}
