package entity;

import java.time.LocalDate;

import enums.InternshipStatus;
import enums.InternshipLevel;


public class ReportFilter {

    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReportFilter() {
        clearFilters();
    }

    public boolean applyFilter(Internship internship) {
        if (!isValidFilter()) {
            return true;
        }

        boolean matches = true;

        if (status != null && status != internship.getStatus()) {
            matches = false;
        }
        // Major
        if (preferredMajor != null &&
                (internship.getPreferredMajor() == null ||
                 !preferredMajor.equalsIgnoreCase(internship.getPreferredMajor()))) {
            matches = false;
        }

        // Level
        if (level != null && level != internship.getLevel()) {
            matches = false;
        }
        if (companyName != null &&
                (internship.getCompany() == null ||
                 !companyName.equalsIgnoreCase(internship.getCompany()))) {
            matches = false;
        }
        if (startDate != null) {
            LocalDate open = internship.getOpenDate();
            if (open == null || open.isBefore(startDate)) {
                matches = false;
            }
        }
        if (endDate != null) {
            LocalDate close = internship.getClosingDate();
            if (close == null || close.isAfter(endDate)) {
                matches = false;
            }
        }

        return matches;
    }

    public void clearFilters() {
        this.status = null;
        this.preferredMajor = null;
        this.level = null;
        this.companyName = null;
        this.startDate = null;
        this.endDate = null;
    }

    public boolean isValidFilter() {
        return status != null ||
               preferredMajor != null ||
               level != null ||
               companyName != null ||
               startDate != null ||
               endDate != null;
    }

    // ---------- Getters / Setters ----------

    public InternshipStatus getStatus() {
        return status;
    }

    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
