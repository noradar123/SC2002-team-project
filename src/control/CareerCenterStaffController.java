package control;

import java.util.List;

import boundary.CareerCenterStaffView;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Internship;
import entity.Application;
import service.InternshipService;

public class CareerCenterStaffController {
    private final CareerCenterStaffView view;
    private final AccountCreationController accountController;
    private final InternshipService internshipService;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    public CareerCenterStaffController(CareerCenterStaffView view,
                                       AccountCreationController accountController,
                                       InternshipService internshipService,
                                       ApplicationController applicationController,
                                       FilterController filterController) {
        this.view = view;
        this.accountController = accountController;
        this.internshipService = internshipService;
        this.applicationController = applicationController;
        this.filterController = filterController;
    }

    public void showMain(CareerCenterStaff staff) {
        while (true) {
            int opt = view.promptMainMenu();
            switch (opt) {
                case 0: return;
                case 1: manageCompanyReps(); break;
                case 2: manageInternships(); break;
                case 3: manageWithdrawals(); break;
                case 4: manageFilters(staff); break;
                default: view.show("Unknown option.");
            }
        }
    }

    // ============================================================
    //                  COMPANY REP APPROVAL
    // ============================================================
    private void manageCompanyReps() {
        List<CompanyRep> pending = accountController.getPendingCompanyReps();

        if (pending.isEmpty()) {
            view.show("No pending company representative accounts.");
            return;
        }

        view.listCompanyReps(pending);
        int idx = view.promptIndexSelection(pending.size());

        CompanyRep chosen = pending.get(idx);
        String action = view.promptApproveOrReject();

        try {
            if (action.equals("approve")) {
                accountController.approveCompanyRep(chosen.getUserId());
                view.show("Authorized (activated): " + chosen.getUserId());
            } else {
                accountController.rejectCompanyRep(chosen.getUserId());
                view.show("Rejected and removed: " + chosen.getUserId());
            }
        } catch (IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // ============================================================
    //                  INTERNSHIP APPROVAL
    // ============================================================
    private void manageInternships() {
        List<Internship> all = internshipService.getAllInternships();

        if (all.isEmpty()) {
            view.show("No internships available.");
            return;
        }
        view.listInternships(all);
        int idx = view.promptIndexSelection(all.size());
        Internship chosen = all.get(idx);
        String action = view.promptApproveOrReject();
        try {
            if (action.equals("approve")) {
                internshipService.approveInternship(chosen.getId());
                view.show("Approved and made visible: " + chosen.getTitle());
            } else {
                internshipService.rejectInternship(chosen.getId());
                view.show("Rejected: " + chosen.getTitle());
            }
        } catch (IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // ============================================================
    //                  WITHDRAWAL APPROVAL
    // ============================================================
    private void manageWithdrawals() {
        List<Application> pending = applicationController.getPendingWithdrawalRequest();

        if (pending.isEmpty()) {
            view.show("No pending withdrawal requests.");
            return;
        }

        view.listWithdrawalRequests(pending);
        int idx = view.promptIndexSelection(pending.size());
        Application chosen = pending.get(idx);
        String action = view.promptApproveOrReject();

        try {
            if (action.equals("approve")) {
                applicationController.approveWithdrawal(chosen.getApplicationID());
                view.show("Withdrawal approved: " + chosen.getApplicationID());
            } else {
                applicationController.rejectWithdrawal(chosen.getApplicationID());
                view.show("Withdrawal rejected: " + chosen.getApplicationID());
            }

        } catch (IllegalStateException | IllegalArgumentException e) {
            view.show(e.getMessage());
        }
    }

    // ============================================================
    //                  FILTER MANAGEMENT
    // ============================================================
    private void manageFilters(CareerCenterStaff staff) {
        filterController.manageFiltersFor(staff);
    }
}
