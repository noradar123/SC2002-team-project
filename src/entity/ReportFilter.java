import java.time.LocalDate;

public class ReportFilter {
    // Attributes
    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructors
    public ReportFilter() {
        clearFilters();
    }

    // Methods
    public boolean applyFilter(InternshipOpportunity opportunity) {
        if (!isValidFilter()) {
            return true;
        }

        boolean matches = true;

        if (status != null && !status.equals(opportunity.getStatus())) {
            matches = false;
        }

        if (preferredMajor != null && !preferredMajor.equalsIgnoreCase(opportunity.getMajor())) {
            matches = false;
        }

        if (level != null && !level.equals(opportunity.getLevel())) {
            matches = false;
        }

        if (companyName != null && !companyName.equalsIgnoreCase(opportunity.getCompanyName())) {
            matches = false;
        }

        if (startDate != null && (opportunity.getStartDate() == null || opportunity.getStartDate().isBefore(startDate))) {
            matches = false;
        }

        if (endDate != null && (opportunity.getEndDate() == null || opportunity.getEndDate().isAfter(endDate))) {
            matches = false;
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
        return status != null || preferredMajor != null || level != null ||
               companyName != null || startDate != null || endDate != null;
    }

    // Getters and Setters
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