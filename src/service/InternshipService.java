package service;

import data.InternshipRepository;
import data.ApplicationRepository;
import entity.Internship;
import entity.User;
import entity.CompanyRep;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import enums.InternshipLevel;
import enums.InternshipStatus;

public class InternshipService {

    private final InternshipRepository repo;

    public InternshipService(InternshipRepository repo) {
        this.repo = repo;
    }

    // return a list of internships based on the user filter settings
    public List<Internship> getInternshipsFor(User user) {
        return repo.all().stream()
                .filter(i -> user.getFilter().matches(i))
                .collect(Collectors.toList());
    }

    // return internships for a particular company
    public List<Internship> getInternshipsFor(String company) {
        return repo.all().stream()
                .filter(i -> i.getCompany().equals(company))
                .collect(Collectors.toList());
    }

    // Create a new internship (called by CompanyRepController)
    public Internship createInternship(CompanyRep rep, String title, String description,
                                       InternshipLevel level, String preferredMajor,
                                       LocalDate openDate, LocalDate closeDate, int slots) {
        if (rep == null) throw new IllegalArgumentException("CompanyRep cannot be null");
        if (!rep.isAuthorized()) throw new IllegalStateException("You are not approved by the Career Centre Staff.");
        if (rep.getNumberOfInternships() >= 5) throw new IllegalStateException("Max 5 internships reached.");
        if (openDate == null || closeDate == null) throw new IllegalArgumentException("Open and close dates required");
        if (!openDate.isBefore(closeDate)) throw new IllegalArgumentException("Open date must be before close date.");
        if (slots <= 0) throw new IllegalArgumentException("Slots must be a positive integer.");

        Internship internship = new Internship(title, description, level, preferredMajor, openDate, closeDate, rep.getCompany(), slots);
        repo.add(internship);
        rep.incrementInternships();
        return internship;
    }

    // Edit internship if status is PENDING - overloaded helper used by Controller
    public Internship editInternship(String id, String title, String description, String preferredMajor,
                                     String levelStr, LocalDate openDate, LocalDate closeDate, Boolean visibility) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        if (existing.getStatus() != null && existing.getStatus() != InternshipStatus.PENDING) {
            throw new IllegalStateException("Cannot edit internship unless status is PENDING");
        }

        if (title != null) existing.setTitle(title);
        if (description != null) existing.setDescription(description);
        if (preferredMajor != null) existing.setPreferredMajor(preferredMajor);

        if (levelStr != null) {
            try {
                InternshipLevel level = InternshipLevel.valueOf(levelStr.toUpperCase());
                existing.setLevel(level);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid level: " + levelStr);
            }
        }

        if (openDate != null && closeDate != null) {
            if (!openDate.isBefore(closeDate)) throw new IllegalArgumentException("Open date must be before close date.");
        }
        if (openDate != null) existing.setOpenDate(openDate);
        if (closeDate != null) existing.setCloseDate(closeDate);

        if (visibility != null) existing.setVisible(visibility);

        return repo.update(existing);
    }

    // Existing edit method that accepts a full Internship object
    public void editInternship(String id, Internship updated) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        if (existing.getStatus() != null && existing.getStatus().name().equals("PENDING")) {
            repo.update(updated);
        } else {
            throw new IllegalStateException("Cannot edit internship unless status is PENDING");
        }
    }

    // Delete internship (only owner and only when pending)
    public void deleteInternship(CompanyRep rep, String id) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        if (!existing.getCompany().equals(rep.getCompany())) {
            throw new IllegalStateException("Not authorized to delete this internship.");
        }
        if (existing.getStatus() != null && existing.getStatus() != InternshipStatus.PENDING) {
            throw new IllegalStateException("Only pending internships can be deleted.");
        }
        boolean removed = repo.remove(id);
        if (!removed) throw new IllegalStateException("Failed to delete internship: " + id);
        rep.decrementInternships();
    }

    // Set visibility
    public void setVisibility(String id, boolean visible) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setVisible(visible);
        repo.update(existing);
    }

    // Mark as filled
    public void markAsFilled(Internship internship) {
        if (internship.getFilledSlots() >= internship.getSlots()) {
            internship.setVisible(false);
            internship.setStatus(InternshipStatus.FILLED);
        }
    }

    // --- New admin helpers ---
    public java.util.List<Internship> getAllInternships() {
        return repo.all();
    }

    public void approveInternship(String id) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setStatus(InternshipStatus.APPROVED);
        existing.setVisible(true);
        repo.update(existing);
    }

    public void rejectInternship(String id) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setStatus(InternshipStatus.REJECTED);
        existing.setVisible(false);
        repo.update(existing);
    }
}