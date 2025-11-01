package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import enums.InternshipStatus;
import enums.WithdrawalStatus;

import entity.Internship;
import entity.WithdrawalRequest;
import entity.ReportFilter;
import entity.Report;


public class CareerCenterStaff extends Users {
    private String staffDepartment;

    public CareerCenterStaff(String userId, String name, String password, String staffDepartment) {
        super(userId, name, password);
        this.staffDepartment = staffDepartment;
    }

    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }

    public boolean authorizeCompanyRep(CompanyRep rep) {
        if (rep == null) return false;
        rep.setAuthorized(true);
        return true;
    }

    public boolean rejectCompanyRep(CompanyRep rep) {
        if (rep == null) return false;
        rep.setAuthorized(false);
        return true;
    }

    public boolean approveInternship(Internship intern) {
        if (intern == null) return false;
        intern.setStatus(InternshipStatus.APPROVED);
        intern.setVisibility(true);
        return true;
    }

    public boolean rejectInternship(Internship intern) {
        if (intern == null) return false;
        intern.setStatus(InternshipStatus.REJECTED);
        intern.setVisibility(false);
        return true;
    }

    public boolean approveWithdrawal(WithdrawalRequest req) {
        if (req == null) return false;
        req.setStatus(WithdrawalStatus.APPROVED);
        return true;
    }

    public boolean rejectWithdrawal(WithdrawalRequest req) {
        if (req == null) return false;
        req.setStatus(WithdrawalStatus.REJECTED);
        return true;
    }

    public Report generateReport(ReportFilter filter, List<Internship> allOpportunities) {
        List<Internship> included = new ArrayList<>();
        for (Internship i : allOpportunities) {
            if (filter.matches(i)) {
                included.add(i);
            }
        }
        return new Report(
                UUID.randomUUID().toString(),
                this,
                LocalDate.now(),
                included
        );
    }
}
