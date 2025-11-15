package entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import enums.InternshipStatus;
import enums.WithdrawalStatus;
import service.InternshipServiceImpl;
import store.InternshipRepository;

public class CareerCenterStaff extends User {
    private String staffDepartment;
    private InternshipServiceImpl internshipService;  // Injected service

    // Constructor that now accepts InternshipServiceImpl as a parameter
    public CareerCenterStaff(String userId, String name, String password, String staffDepartment, InternshipServiceImpl internshipService) {
        super(userId, name, password);
        this.staffDepartment = staffDepartment;
        this.internshipService = internshipService;  // Assign the injected service
    }

    // Getter and Setter for staff department
    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }

    // Authorize or reject a Company Representative
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

    // Approve or reject internships
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

    // Handle Withdrawal Requests
    public boolean decideWithdrawal(WithdrawalRequest req, boolean approve) {
        if (req == null || req.getStatus() == null || req.getStatus() == WithdrawalStatus.PENDING) {
            return false;
        }

        if (approve) {
            req.setStatus(WithdrawalStatus.APPROVED);
        } else {
            req.setStatus(WithdrawalStatus.REJECTED);
        }
        return true;
    }

    // Filter Internships based on the parameters
    public List<Internship> filterInternships(String status, String level, String major) {
        // Build the filter map
        Map<String, String> filters = new HashMap<>();

        if (status != null) filters.put("status", status);
        if (level != null) filters.put("level", level);
        if (major != null) filters.put("major", major);

        // Call the service to filter internships
        return internshipService.filterInternships(filters);
    }


    // Generate Report based on filtered internships
    public Report generateReport(List<Internship> filteredList) {
        String reportId = UUID.randomUUID().toString();
        LocalDate today = LocalDate.now();

        return new Report(reportId, this, today, filteredList);
    }
}