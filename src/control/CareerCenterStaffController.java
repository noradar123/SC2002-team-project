package control;

import java.util.List;

import boundary.CareerCenterStaffView;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Internship;
import entity.Application;
import service.InternshipService;

/**
 * Controller responsible for managing the workflows specific to Career Center Staff.
 * This includes administrative tasks such as approving Company Representatives,
 * vetting new Internship listings, and processing student withdrawal requests.
 */
public class CareerCenterStaffController {
    private final CareerCenterStaffView view;
    private final AccountCreationController accountController;
    private final InternshipService internshipService;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    /**
     * Constructs a new CareerCenterStaffController.
     *
     * @param view                  The UI view for staff interactions.
     * @param accountController     The controller for managing user accounts (approvals).
     * @param internshipService     The service for accessing and modifying internship data.
     * @param applicationController The controller for handling student application withdrawals.
     * @param filterController      The controller for managing search filters.
     */
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

    /**
     * Displays the main menu and handles the primary navigation for a Career Center Staff member.
     * Keeps the session active until the user chooses to log out.
     *
     * @param staff The currently logged-in Staff member.
     */
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

    /**
     * Handles the workflow for approving or rejecting pending Company Representative accounts.
     * Fetches pending requests, prompts the staff for a decision, and delegates to the AccountController.
     */
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

    /**
     * Handles the workflow for approving or rejecting Internship listings.
     * Fetches internships, allows selection, and updates their status via the InternshipService.
     */
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

    /**
     * Handles the workflow for approving or rejecting student withdrawal requests.
     * Fetches pending applications, prompts for decision, and delegates to the ApplicationController.
     */
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
    
    /**
     * Delegates filter management to the FilterController.
     * @param staff The staff member modifying their view filters.
     */
    private void manageFilters(CareerCenterStaff staff) {
        filterController.manageFiltersFor(staff);
    }
}