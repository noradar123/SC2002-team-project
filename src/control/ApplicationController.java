package control;

import entity.Application;
import entity.Student;
import entity.Internship;
import data.ApplicationRepository;
import data.InternshipRepository;
import data.UserWriteRepository;
import enums.ApplicationStatus;
import enums.InternshipLevel;
import enums.InternshipStatus;
import java.util.List;

/**
 * Controller responsible for managing the lifecycle of Internship Applications.
 * Handles logic for creating applications, processing approvals/rejections, 
 * managing student acceptances, and handling withdrawal requests.
 */
public class ApplicationController {
    
    private ApplicationRepository applicationRepository;
    private InternshipRepository internshipRepository;
    private UserWriteRepository userRepository;
    private static final int MAX_APPLICATIONS_PER_STUDENT = 3;

    /**
     * Constructs a new ApplicationController.
     *
     * @param applicationRepository The repository for application data access.
     * @param internshipRepository  The repository for internship data access.
     * @param userRepository        The repository for persisting user data changes (e.g., student state).
     */
    public ApplicationController(ApplicationRepository applicationRepository, InternshipRepository internshipRepository, UserWriteRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
    }

    // ========== GETTERS ========== 

    /**
     * Retrieves all applications in the system.
     * @return A list of all applications.
     */
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * Retrieves a specific application by its ID.
     *
     * @param applicationId The unique ID of the application.
     * @return The Application object.
     * @throws IllegalArgumentException If the application is not found.
     */
    public Application getApplicationById(String applicationId) {
        Application app = applicationRepository.findById(applicationId);
        if (app == null) {
            throw new IllegalArgumentException("Application not found: " + applicationId);
        }
        return app;
    }

    /**
     * Retrieves all applications submitted by a specific student.
     * @param studentId The student's ID.
     * @return A list of applications.
     */
    public List<Application> getApplicationByStudentId(String studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    /**
     * Retrieves all applications for a specific internship title.
     * @param internshipTitle The title of the internship.
     * @return A list of applications.
     */
    public List<Application> getApplicationByInternshipTitle(String internshipTitle) {
        return applicationRepository.findByInternshipTitle(internshipTitle);
    }

    /**
     * Retrieves all applications that have a pending withdrawal request.
     * Used by Career Center Staff to review withdrawals.
     * @return A list of applications with pending withdrawals.
     */
    public List<Application> getPendingWithdrawalRequest() {
        return applicationRepository.findPendingWithdrawalRequest();
    }

    // ========== CREATE OPERATIONS ========== 

    /**
     * Creates a new internship application for a student.
     * Validates eligibility based on:
     * <ul>
     * <li>Maximum active applications (Limit: 3).</li>
     * <li>Existing successful applications.</li>
     * <li>Student year vs. Internship level (Year 1/2 restricted to BASIC).</li>
     * <li>Internship status (Must be APPROVED and not FILLED).</li>
     * <li>Application deadlines.</li>
     * </ul>
     *
     * @param student    The student applying.
     * @param internship The internship being applied for.
     * @return The newly created and saved Application object.
     * @throws IllegalArgumentException If any validation rule is violated.
     * @throws IllegalStateException    If the application could not be created in the student object.
     */
    public Application createApplication(Student student, Internship internship) {
        // check if student can apply how many applications (max 3) 
        int activeApplications = applicationRepository.countActiveApplicationsByStudent(student.getUserId());
        if (activeApplications >= MAX_APPLICATIONS_PER_STUDENT) {
            throw new IllegalArgumentException("Student has reached the maximum 3 active applications.");
        }

        // Check if student already has a successful application
        if (applicationRepository.hasSuccessfulApplication(student.getUserId())) {
            throw new IllegalArgumentException("Student already has a successful application.");
        }

        // Year 1 and 2 can only apply for BASIC level Internship
        if (student.getYearOfStudy() <= 2 && internship.getLevel() != InternshipLevel.BASIC) {
            throw new IllegalArgumentException("Year 1 and 2 students are only eligible for Basic-Level Internship.");
        }
        // Check if internship is eligible for application 
        if (internship.getStatus() != InternshipStatus.APPROVED) {
            throw new IllegalArgumentException("Internship is no longer eligible for application.");
        }
        // Check if internship is filled 
        if (internship.getStatus() == InternshipStatus.FILLED) {
            throw new IllegalArgumentException("Internship opportunity has been filled.");
        }
        // Check whether application deadline has passed 
        if (internship.isClosingDatePassed()) {
            throw new IllegalArgumentException("Application closing date has passed.");
        }

        // create application via Student.applyFor so student's internal list is updated
        boolean applied = student.applyFor(internship);
        if (!applied) {
            throw new IllegalStateException("Failed to create application for student.");
        }
        
        // retrieve newly created application from student's list (assumes added to end)
        List<Application> studentApps = student.getAppliedInternships();
        Application newApp = studentApps.get(studentApps.size() - 1);
        
        // persist application
        Application saved = applicationRepository.save(newApp);
        
        // persist student changes
        if (userRepository != null) {
            userRepository.save(student);
        }
        return saved;
    }

    // ========== COMPANY REP ACTIONS ==========

    /**
     * Approves a student's application.
     * Updates the status to SUCCESSFUL and increments the internship's filled slots.
     *
     * @param applicationId The ID of the application to approve.
     * @return The updated Application object.
     * @throws IllegalStateException If the student already has a job, the application is not pending, or the internship is full.
     */
    public Application approveApplication(String applicationId) {
        Application application = getApplicationById(applicationId);

        // Check if student already has a successful application
        if (applicationRepository.hasSuccessfulApplication(application.getStudent().getUserId())) {
            throw new IllegalStateException("Student already has a successful application.");
        }

        // Check if application is pending
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending applications can be approved.");
        }

        // CRITICAL CHECK: Check if internship is already full BEFORE approving
        Internship internship = application.getInternship();
        if (internship.getFilledSlots() >= internship.getSlots()) {
            throw new IllegalStateException("Internship has already been filled.");
        }

        // Mark as successful
        application.markSuccessful();
        Application updated = applicationRepository.update(application);

        // Update internship filled slots/status after approval
        // We count from the repo to ensure the count is accurate based on actual successful applications
        int confirmedSlots = applicationRepository.countSuccessfulApplicationByInternship(internship.getTitle());
        internship.setFilledSlots(confirmedSlots);

        if (confirmedSlots >= internship.getSlots()) {
            internship.setStatus(InternshipStatus.FILLED);
        }
        
        if (internshipRepository != null) {
            internshipRepository.update(internship);
        }
        return updated;
    }

    /**
     * Rejects a student's application.
     * Updates the status to UNSUCCESSFUL and recalculates the internship's filled slots logic.
     *
     * @param applicationId The ID of the application to reject.
     * @return The updated Application object.
     */
    public Application rejectApplication(String applicationId) {
        Application application = getApplicationById(applicationId);

        // Mark as unsuccessful
        application.markUnsuccessful();
        Application updated = applicationRepository.update(application);

        // Always recalculate internship filled slots/status after rejection
        Internship internship = updated.getInternship();
        int confirmedSlots = applicationRepository.countSuccessfulApplicationByInternship(internship.getTitle());
        internship.setFilledSlots(confirmedSlots);

        // If slots opened up (count < max) and it was previously FILLED, reopen it to APPROVED
        if (confirmedSlots < internship.getSlots() && internship.getStatus() == InternshipStatus.FILLED) {
            internship.setStatus(InternshipStatus.APPROVED);
        }
        
        if (internshipRepository != null) {
            internshipRepository.update(internship);
        }
        return updated;
    }
    // ========== STUDENT ACTIONS ========== 

    /**
     * Processes a student's acceptance of an internship offer.
     * Verifies the application is SUCCESSFUL, then triggers the student entity logic 
     * to accept the offer and auto-withdraw other active applications.
     *
     * @param applicationId The ID of the application being accepted.
     * @return The accepted Application object.
     * @throws IllegalStateException If the application is not in SUCCESSFUL status or acceptance fails.
     */
    public Application acceptPlacement(String applicationId) {
        Application application = getApplicationById(applicationId);
        // only can accept successful applications 
        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Can only accept successful applications");
        }
        Student student = application.getStudent();
        
        // Let student entity update its accepted application and withdraw others
        boolean accepted = student.accept(application);
        if (!accepted) {
            throw new IllegalStateException("Student could not accept the application");
        }
        
        // persist student changes
        if (userRepository != null) {
            userRepository.save(student);
        }
        
        // persist all applications for this student so withdrawn flags are saved
        List<Application> apps = applicationRepository.findByStudentId(student.getUserId());
        for (Application a : apps) {
            applicationRepository.update(a);
        }
        
        // persist accepted application as well
        applicationRepository.update(application);
        
        // update internship filled slots/status
        Internship internship = application.getInternship();
        int confirmedSlots = applicationRepository.countSuccessfulApplicationByInternship(internship.getTitle());
        internship.setFilledSlots(confirmedSlots);
        
        if (confirmedSlots >= internship.getSlots()) {
            internship.setStatus(InternshipStatus.FILLED);
        }
        if (internshipRepository != null) {
            internshipRepository.update(internship);
        }
        return application;
    }

    /**
     * Flags an application for withdrawal.
     * Requires Career Center Staff approval to finalize.
     *
     * @param applicationId The ID of the application to withdraw.
     * @return The updated Application object.
     */
    public Application requestWithdrawal(String applicationId) {
        Application application = getApplicationById(applicationId);
        application.requestWithdrawal();
        return applicationRepository.update(application);
    }

    // ========== CAREER CENTER STAFF ACTIONS ========== 

    /**
     * Approves a pending withdrawal request.
     * Finalizes the withdrawal and updates internship slot counts if necessary.
     *
     * @param applicationId The ID of the application with the withdrawal request.
     * @return The updated Application object.
     * @throws IllegalStateException If no withdrawal request exists.
     */
    public Application approveWithdrawal(String applicationId) {
        Application application = getApplicationById(applicationId);
        if (!application.isWithdrawalRequested()) {
            throw new IllegalStateException("No withdrawal request found for this application");
        }
        // if application is active and successful, we should update related internship filled slots
        application.markWithDrawn();
        Application updated = applicationRepository.update(application);
        
        // update internship filled slots/status if needed
        Internship internship = updated.getInternship();
        int confirmedSlots = applicationRepository.countSuccessfulApplicationByInternship(internship.getTitle());
        internship.setFilledSlots(confirmedSlots);
        
        if (confirmedSlots < internship.getSlots() && internship.getStatus() == InternshipStatus.FILLED) {
            internship.setStatus(InternshipStatus.APPROVED);
        }
        if (internshipRepository != null) {
            internshipRepository.update(internship);
        }
        return updated;
    }

    /**
     * Rejects a withdrawal request.
     * Clears the withdrawal requested flag, returning the application to its previous state.
     *
     * @param applicationId The ID of the application.
     * @return The updated Application object.
     * @throws IllegalStateException If no withdrawal request exists.
     */
    public Application rejectWithdrawal(String applicationId) {
        Application application = getApplicationById(applicationId);
        // Must have withdrawal request 
        if (!application.isWithdrawalRequested()) {
            throw new IllegalStateException("No withdrawal request found for this application");
        }
        // Clear withdrawal request flag 
        application.setWithdrawalRequested(false);
        return applicationRepository.update(application);
    }

    /**
     * Deletes an application from the system.
     * Only allows deletion of PENDING applications.
     *
     * @param applicationId The ID of the application to delete.
     * @throws IllegalStateException If the application is not PENDING.
     */
    public void deleteApplication(String applicationId) {
        Application application = getApplicationById(applicationId);
        // Can only delete pending application
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only delete pending application.");
        }
        applicationRepository.delete(applicationId);
    }
}