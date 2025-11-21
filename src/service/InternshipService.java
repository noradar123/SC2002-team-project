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

/**
 * Service class responsible for managing Internship entities.
 * Handles business logic for creating, retrieving, updating, and deleting internships,
 * including validation rules for dates, slots, and company representative permissions.
 */
public class InternshipService {

    private final InternshipRepository repo;
    /**
     * Constructs a new InternshipService.
     *
     * @param repo The repository used for data access to Internship objects.
     */
    public InternshipService(InternshipRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves a list of internships filtered by the user's specific preferences.
     *
     * @param user The user whose filter settings should be applied.
     * @return A list of {@link Internship} objects matching the user's criteria.
     */
    public List<Internship> getInternshipsFor(User user) {
        return repo.all().stream()
                .filter(i -> user.getFilter().matches(i))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all internships posted by a specific company.
     *
     * @param company The name of the company to filter by.
     * @return A list of {@link Internship} objects belonging to that company.
     */
    public List<Internship> getInternshipsFor(String company) {
        return repo.all().stream()
                .filter(i -> i.getCompany().equals(company))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new internship listing.
     * Validates that the Company Representative is authorized, has not exceeded their quota (max 5),
     * and that the provided dates and slots are valid.
     *
     * @param rep            The Company Representative creating the internship.
     * @param title          The title of the internship.
     * @param description    The description of the role.
     * @param level          The professional level (e.g., JUNIOR, SENIOR).
     * @param preferredMajor The preferred major for applicants.
     * @param openDate       The date applications open.
     * @param closeDate      The date applications close.
     * @param slots          The number of available positions.
     * @return The newly created {@link Internship} object.
     * @throws IllegalArgumentException If input data is invalid (nulls, dates out of order, non-positive slots).
     * @throws IllegalStateException    If the rep is unauthorized or has reached the maximum number of internships.
     */
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

    /**
     * Edits specific fields of an existing internship using raw values.
     * Only internships with {@code PENDING} status can be edited.
     *
     * @param id             The unique ID of the internship to edit.
     * @param title          The new title (or null to keep existing).
     * @param description    The new description (or null to keep existing).
     * @param preferredMajor The new preferred major (or null to keep existing).
     * @param levelStr       The new level as a String (or null to keep existing).
     * @param openDate       The new open date (or null to keep existing).
     * @param closeDate      The new close date (or null to keep existing).
     * @param visibility     The new visibility status (or null to keep existing).
     * @return The updated {@link Internship} object.
     * @throws IllegalArgumentException If the internship is not found, level string is invalid, or dates are invalid.
     * @throws IllegalStateException    If the internship status is not PENDING.
     */
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

    /**
     * Updates an existing internship with a new Internship object.
     * Only internships with {@code PENDING} status can be edited.
     *
     * @param id      The unique ID of the internship to edit.
     * @param updated The {@link Internship} object containing new data.
     * @throws IllegalArgumentException If the internship is not found.
     * @throws IllegalStateException    If the internship status is not PENDING.
     */
    public void editInternship(String id, Internship updated) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        if (existing.getStatus() != null && existing.getStatus().name().equals("PENDING")) {
            repo.update(updated);
        } else {
            throw new IllegalStateException("Cannot edit internship unless status is PENDING");
        }
    }

    /**
     * Deletes an internship listing.
     * Only the owner of the internship (matching Company) can delete it, and only if it is currently PENDING.
     *
     * @param rep The Company Representative attempting to delete the internship.
     * @param id  The unique ID of the internship to delete.
     * @throws IllegalArgumentException If the internship is not found.
     * @throws IllegalStateException    If the rep is not the owner, or the internship is not PENDING.
     */
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

    /**
     * Toggles the visibility of a specific internship.
     *
     * @param id      The unique ID of the internship.
     * @param visible {@code true} to make it visible, {@code false} to hide it.
     * @throws IllegalArgumentException If the internship is not found.
     */
    public void setVisibility(String id, boolean visible) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setVisible(visible);
        repo.update(existing);
    }

    /**
     * Checks if an internship's slots are full and updates its status if necessary.
     * If filled, sets status to {@code FILLED} and visibility to {@code false}.
     *
     * @param internship The internship to check.
     */
    public void markAsFilled(Internship internship) {
        if (internship.getFilledSlots() >= internship.getSlots()) {
            internship.setVisible(false);
            internship.setStatus(InternshipStatus.FILLED);
        }
    }

    // --- New admin helpers ---
    /**
     * Retrieves a comprehensive list of all internships in the system.
     * This is typically used by administrators.
     *
     * @return A list of all {@link Internship} objects.
     */
    public java.util.List<Internship> getAllInternships() {
        return repo.all();
    }
    /**
     * Approves an internship listing.
     * Sets the status to {@code APPROVED} and ensures it is visible.
     *
     * @param id The unique ID of the internship to approve.
     * @throws IllegalArgumentException If the internship is not found.
     */
    public void approveInternship(String id) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setStatus(InternshipStatus.APPROVED);
        existing.setVisible(true);
        repo.update(existing);
    }
    /**
     * Rejects an internship listing.
     * Sets the status to {@code REJECTED} and hides it from view.
     *
     * @param id The unique ID of the internship to reject.
     * @throws IllegalArgumentException If the internship is not found.
     */
    public void rejectInternship(String id) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        existing.setStatus(InternshipStatus.REJECTED);
        existing.setVisible(false);
        repo.update(existing);
    }
}