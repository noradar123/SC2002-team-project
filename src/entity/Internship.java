package entity;

import java.time.LocalDate;
import enums.InternshipLevel;
import enums.InternshipStatus;
import java.util.UUID;

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

    // Constructor
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

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public InternshipLevel getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpenDate() { return openDate; }
    public LocalDate getClosingDate() { return closeDate; }
    public InternshipStatus getStatus() { return status; }
    public boolean isVisible() { return visible; }
    public String getCompany() { return company; }
    public int getSlots() { return slots; }
    public int getFilledSlots() { return filledSlots; }

    // Setters 
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(InternshipLevel level) { this.level = level; }
    public void setPreferredMajor(String major) { this.preferredMajor = major; }
    public void setOpenDate(LocalDate openDate) {
        if (closeDate != null && !openDate.isBefore(closeDate)) return;
        this.openDate = openDate;
    }
    public void setCloseDate(LocalDate closeDate) {
        if (openDate != null && !closeDate.isAfter(openDate)) return;
        this.closeDate = closeDate;
    }
    public void setStatus(InternshipStatus status) { this.status = status; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setFilledSlots(int filledSlots) { this.filledSlots = filledSlots; }

    // Utility methods
    public boolean isStudentYearEligible(int year) {
        switch (level) {
            case BASIC: return true;
            case INTERMEDIATE:
            case ADVANCED: return year >= 3;
            default: return false;
        }
    }

    // Update this internship's fields from another internship object
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

    // New helper used by controllers to check if the closing date has passed
    public boolean isClosingDatePassed() {
        if (this.closeDate == null) return false;
        return java.time.LocalDate.now().isAfter(this.closeDate);
    }
}