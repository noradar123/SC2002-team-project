package entity;

import java.time.LocalDate;

import enums.InternshipStatus;
import enums.InternshipLevel;

/**
 * Filter used for report / listing of internships.
 * Works with entity.Internship (not InternshipOpportunity).
 */
public class ReportFilter {

    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private String companyName;
    private LocalDate startDate;  // 對應 Internship.openDate
    private LocalDate endDate;    // 對應 Internship.closeDate

    public ReportFilter() {
        clearFilters();
    }

    /**
     * Return true if the given internship matches current filter.
     * If no filter is set (isValidFilter() == false), always returns true.
     */
    public boolean applyFilter(Internship internship) {
        if (!isValidFilter()) {
            // 沒有設定任何過濾條件 -> 不過濾
            return true;
        }

        boolean matches = true;

        // 狀態
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

        // 公司名稱
        if (companyName != null &&
                (internship.getCompany() == null ||
                 !companyName.equalsIgnoreCase(internship.getCompany()))) {
            matches = false;
        }

        // 開始日期（對應 Internship.openDate）
        if (startDate != null) {
            LocalDate open = internship.getOpenDate();
            if (open == null || open.isBefore(startDate)) {
                matches = false;
            }
        }

        // 結束日期（對應 Internship.closeDate）
        if (endDate != null) {
            LocalDate close = internship.getClosingDate();
            if (close == null || close.isAfter(endDate)) {
                matches = false;
            }
        }

        return matches;
    }

    /** 清空所有 filter 條件（回到「不過濾」狀態） */
    public void clearFilters() {
        this.status = null;
        this.preferredMajor = null;
        this.level = null;
        this.companyName = null;
        this.startDate = null;
        this.endDate = null;
    }

    /** 是否有至少一個條件正在生效 */
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
