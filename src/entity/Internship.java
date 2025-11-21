package entity;

import java.time.LocalDate;
import enums.InternshipLevel;
import enums.InternshipStatus;
import java.util.UUID;

/**
 * Represents a specific internship opportunity listed by a company.
 * This entity holds all details regarding the job description, availability dates,
 * vacancy slots, and its current administrative status.
 */
public class Internship {

    private final String id;          
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private LocalDate openDate;
    private LocalDate closeDate;
    private InternshipStatus status;
    private boolean visible;
    private final String company;
    private final int slots;
    private int filledSlots;

    /**
     * Constructs a new Internship listing.
     * Automatically generates a unique UUID, sets the status to PENDING, 
     * and visibility to false (hidden) by default.
     *
     * @param title          The job title.
     * @param description    The job details/description.
     * @param level          The seniority level (BASIC, INTERMEDIATE, ADVANCED).
     * @param preferredMajor The major preferred for this role.
     * @param openDate       The date applications open.
     * @param closeDate      The date applications close.
     * @param company        The name of the company offering the internship.
     * @param slots          The total number of positions available.
     */
    public Internship(String title, String description, InternshipLevel level,
                      String preferredMajor, LocalDate openDate,
                      LocalDate closeDate, String company, int slots) {

        this.id = UUID.randomUUID().toString(); 
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.company = company;
        this.slots = slots;
        this.filledSlots = 0;
        this.status = InternshipStatus.PENDING;
        this.visible = false;
    }

    // ========== GETTERS ==========

    /**
     * Gets the unique ID of the internship.
     * @return The UUID string.
     */
    public String getId() { return id; }

    /**
     * Gets the job title.
     * @return The title.
     */
    public String getTitle() { return title; }

    /**
     * Gets the job description.
     * @return The description.
     */
    public String getDescription() { return description; }

    /**
     * Gets the seniority level of the internship.
     * @return The InternshipLevel enum.
     */
    public InternshipLevel getLevel() { return level; }

    /**
     * Gets the preferred major.
     * @return The major string.
     */
    public String getPreferredMajor() { return preferredMajor; }

    /**
     * Gets the date applications open.
     * @return The open date.
     */
    public LocalDate getOpenDate() { return openDate; }

    /**
     * Gets the date applications close.
     * @return The closing date.
     */
    public LocalDate getClosingDate() { return closeDate; }

    /**
     * Gets the current administrative status.
     * @return The InternshipStatus enum (e.g., PENDING, APPROVED).
     */
    public InternshipStatus getStatus() { return status; }

    /**
     * Checks if the internship is currently visible to students.
     * @return true if visible, false otherwise.
     */
    public boolean isVisible() { return visible; }

    /**
     * Gets the company name.
     * @return The company string.
     */
    public String getCompany() { return company; }

    /**
     * Gets the total number of slots available.
     * @return The total slots.
     */
    public int getSlots() { return slots; }

    /**
     * Gets the number of slots already filled by accepted applicants.
     * @return The filled slots count.
     */
    public int getFilledSlots() { return filledSlots; }

    // ========== SETTERS ==========

    /**
     * Updates the job title.
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Updates the job description.
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Updates the seniority level.
     * @param level The new InternshipLevel.
     */
    public void setLevel(InternshipLevel level) { this.level = level; }

    /**
     * Updates the preferred major.
     * @param major The new major.
     */
    public void setPreferredMajor(String major) { this.preferredMajor = major; }

    /**
     * Updates the opening date.
     * Validation: The update is ignored if the new date is not before the current closing date.
     * @param openDate The new open date.
     */
    public void setOpenDate(LocalDate openDate) {
        if (closeDate != null && !openDate.isBefore(closeDate)) return;
        this.openDate = openDate;
    }

    /**
     * Updates the closing date.
     * Validation: The update is ignored if the new date is not after the current open date.
     * @param closeDate The new closing date.
     */
    public void setCloseDate(LocalDate closeDate) {
        if (openDate != null && !closeDate.isAfter(openDate)) return;
        this.closeDate = closeDate;
    }

    /**
     * Updates the administrative status.
     * @param status The new status (e.g., APPROVED, FILLED).
     */
    public void setStatus(InternshipStatus status) { this.status = status; }

    /**
     * Updates the visibility of the internship.
     * @param visible true to show to students, false to hide.
     */
    public void setVisible(boolean visible) { this.visible = visible; }

    /**
     * Updates the number of filled slots.
     * @param filledSlots The new count of filled slots.
     */
    public void setFilledSlots(int filledSlots) { this.filledSlots = filledSlots; }

    // ========== UTILITY METHODS ==========

    /**
     * Determines if a student is eligible for this internship based on their year of study.
     * Rule: BASIC level is open to all. INTERMEDIATE/ADVANCED require Year 3 or higher.
     *
     * @param year The student's year of study.
     * @return true if eligible, false otherwise.
     */
    public boolean isStudentYearEligible(int year) {
        switch (level) {
            case BASIC: return true;
            case INTERMEDIATE:
            case ADVANCED: return year >= 3;
            default: return false;
        }
    }

    /**
     * Updates this internship's fields using values from another Internship object.
     * Typically used during an edit operation to bulk-update properties.
     *
     * @param other The source Internship object containing updated data.
     */
    public void updateFrom(Internship other) {
        this.title = other.title;
        this.description = other.description;
        this.level = other.level;
        this.preferredMajor = other.preferredMajor;
        this.openDate = other.openDate;
        this.closeDate = other.closeDate;
        this.status = other.status;
        this.visible = other.visible;
        this.filledSlots = other.filledSlots;
    }

    /**
     * Checks if the application deadline has passed.
     * Compares the closing date to the current system date.
     *
     * @return true if today is strictly after the closing date.
     */
    public boolean isClosingDatePassed() {
        if (this.closeDate == null) return false;
        return java.time.LocalDate.now().isAfter(this.closeDate);
    }
    /**
     * Increments the count of filled slots by one, provided the total slots have not been reached.
     * Used when an application is approved.
     */
    public void incrementFilledSlots() {
    	  if (filledSlots < slots) {
    	   filledSlots++;
    	  }
    }
    /**
     * Decrements the count of filled slots by one, provided the count is greater than zero.
     * Used when a previously approved application is withdrawn or rejected later.
     */
    public void decrementFilledSlots() {
    	if (filledSlots > 0) {
    	filledSlots--;
    	}
    }
}