package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import enums.InternshipLevel;
import enums.InternshipStatus;

public class Internship {

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
    private final List<Application> applicationList = new ArrayList<>();

    public Internship(String title, String description, InternshipLevel level,
                      String preferredMajor, LocalDate openDate,
                      LocalDate closeDate, String company,
                      int totalSlots) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.company = company;
        this.slots = totalSlots;
        this.filledSlots = 0;
        this.status = InternshipStatus.PENDING;
        this.visible = false;
    }

    public String getCompany() { return company; }
    public InternshipStatus getStatus() { return status; }
    public void setStatus(InternshipStatus status) { this.status = status; }
    public String getMajors() { return preferredMajor; }
    public InternshipLevel getLevel() { return level; }
    public LocalDate getClosingDate() { return closeDate; }
    public LocalDate getOpenDate() { return openDate; }
    public boolean isVisible() { return visible; }
    public int getSlots() { return slots; }
    public int getFilledSlots() { return filledSlots; }
    public List<Application> getApplicationList() { return applicationList; }

    public void incrementFilled() { filledSlots++; }

    public boolean canStudentApply(Student student, LocalDate now) {
        if (now.isBefore(openDate) || now.isAfter(closeDate)) return false;
        if (!visible) return false;
        if (status != InternshipStatus.APPROVED) return false;
        if (!student.getMajor().equals(preferredMajor)) return false;
        if (!isStudentYearEligible(student.getYearOfStudy())) return false;
        for (Application app : applicationList) {
            if (app.getStudent().equals(student)) return false;
        }
        return true;
    }

    private boolean isStudentYearEligible(int year) {
        switch (level) {
            case BASIC: return true;
            case INTERMEDIATE:
            case ADVANCED: return year >= 3;
            default: return false;
        }
    }

    public boolean checkIfFull() {
        if (filledSlots >= slots) {
            visible = false;
            status = InternshipStatus.FILLED;
            return true;
        }
        return false;
    }

    public boolean canSet() {
        if (status != InternshipStatus.PENDING) {
            System.out.println("Cannot edit because status is not PENDING.");
            return false;
        }
        return true;
    }

    public void setVisibility(boolean visible) { if (canSet()) this.visible = visible; }
    public void setTitle(String title) { if (canSet()) this.title = title; }
    public void setDescription(String description) { if (canSet()) this.description = description; }
    public void setLevel(InternshipLevel level) { if (canSet()) this.level = level; }
    public void setMajor(String major) { if (canSet()) this.preferredMajor = major; }

    public void setOpenDate(LocalDate openDate) {
        if (openDate == null) { System.out.println("Open date cannot be null; no change is made."); return; }
        if (closeDate != null && !openDate.isBefore(closeDate)) {
            System.out.println("Open date must be before close date; no change is made.");
            return;
        }
        this.openDate = openDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        if (closeDate == null) { System.out.println("Close date cannot be null; no change is made."); return; }
        if (openDate != null && !closeDate.isAfter(openDate)) {
            System.out.println("Close date must be after open date; no change is made.");
            return;
        }
        this.closeDate = closeDate;
    }

    public boolean addApplication(Application application) {
        if (!canStudentApply(application.getStudent(), LocalDate.now())) return false;
        applicationList.add(application);
        filledSlots++;
        return true;
    }

    @Override
    //change later depending on 
    public String toString() {
        return String.format("Internship[title=%s, company=%s, level=%s, major=%s, status=%s, visible=%b, slots=%d/%d, open=%s, close=%s]",
                title, company, level, preferredMajor, status, visible, filledSlots, slots, openDate, closeDate);
    }
}
