package entity;

import java.time.LocalDate;

import enums.InternshipStatus;
import enums.InternshipLevel;

/**
 * A helper class used to filter internships specifically for report generation.
 * This class allows Career Center Staff to define multiple criteria (e.g., date range, status, company)
 * to select which internships should be included in a statistical report.
 */
public class ReportFilter {

    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs a new ReportFilter.
     * Initializes the filter with no active criteria (all fields null).
     */
    public ReportFilter() {
        clearFilters();
    }

    /**
     * Checks if a specific internship matches the criteria defined in this filter.
     * If no criteria are set (i.e., {@link #isValidFilter()} is false), the internship matches by default.
     *
     * @param internship The internship to check.
     * @return {@code true} if the internship matches all set criteria; {@code false} otherwise.
     */
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

    /**
     * Resets all filtering criteria to null.
     * After calling this, {@link #applyFilter(Internship)} will return true for any internship.
     */
    public void clearFilters() {
        this.status = null;
        this.preferredMajor = null;
        this.level = null;
        this.companyName = null;
        this.startDate = null;
        this.endDate = null;
    }

    /**
     * Checks if any filtering criteria have been set.
     *
     * @return {@code true} if at least one filter field is not null; {@code false} if the filter is empty.
     */
    public boolean isValidFilter() {
        return status != null ||
               preferredMajor != null ||
               level != null ||
               companyName != null ||
               startDate != null ||
               endDate != null;
    }

    // ---------- Getters / Setters ----------

    /**
     * Gets the status filter criterion.
     * @return The status to filter by.
     */
    public InternshipStatus getStatus() {
        return status;
    }

    /**
     * Sets the status filter criterion.
     * @param status The status to filter by.
     */
    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    /**
     * Gets the preferred major filter criterion.
     * @return The major string.
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Sets the preferred major filter criterion.
     * @param preferredMajor The major to filter by.
     */
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    /**
     * Gets the internship level filter criterion.
     * @return The level.
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Sets the internship level filter criterion.
     * @param level The level to filter by.
     */
    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    /**
     * Gets the company name filter criterion.
     * @return The company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name filter criterion.
     * @param companyName The company name to filter by.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the start date for the filter range.
     * @return The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date for the filter range.
     * Internships opening before this date will be excluded.
     * @param startDate The start date.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date for the filter range.
     * @return The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date for the filter range.
     * Internships closing after this date will be excluded.
     * @param endDate The end date.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}