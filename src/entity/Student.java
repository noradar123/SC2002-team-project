package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enums.ApplicationStatus;
import enums.InternshipLevel;
import filter.StudentFilter;

/**
 * Represents a Student user in the system.
 * Tracks the student's academic details (Year, Major) and manages their
 * internship applications, including the limit of concurrent applications (Max 3)
 * and eligibility rules based on seniority.
 */
public class Student extends User {
    
    private final int yearOfStudy;
    private final String major;
    private final List<Application> applications;  // up to 3 concurrent
    private Application acceptedApplication;       // 0..1

    /**
     * Constructs a new Student user.
     * Validates that the year of study is between 1 and 4.
     * Initializes the student with a specific {@link StudentFilter} to manage their view of internships.
     *
     * @param userId      The unique user ID.
     * @param name        The student's full name.
     * @param password    The login password.
     * @param yearOfStudy The year of study (1-4).
     * @param major       The student's major.
     * @throws IllegalArgumentException If yearOfStudy is not 1-4 or major is null.
     */
    public Student(String userId, String name, String password,
                   int yearOfStudy, String major) {
        super(userId, name, password); // placeholder, will set properly below
        if (yearOfStudy < 1 || yearOfStudy > 4) {
            throw new IllegalArgumentException("yearOfStudy must be between 1 and 4");
        }
        if (major == null) {
            throw new IllegalArgumentException("major must not be null");
        }
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.acceptedApplication = null;
        
        // this one is for filtering internships for the student
        super.setFilter(new StudentFilter(this));
    }

    /**
     * Gets the student's current year of study.
     * @return The year (1-4).
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Gets the student's major.
     * @return The major string.
     */
    public String getMajor() {
        return major;
    }

    /**
     * Retrieves a read-only list of all applications submitted by this student.
     * @return An unmodifiable list of {@link Application} objects.
     */
    public List<Application> getAppliedInternships() {
        return Collections.unmodifiableList(applications);
    }

    /**
     * Checks if the student has successfully accepted an internship placement.
     * @return {@code true} if an application has been accepted.
     */
    public boolean hasAcceptedPlacement() {
        return acceptedApplication != null;
    }

    /**
     * Retrieves the specific application that was accepted by the student.
     * @return The accepted {@link Application}, or {@code null} if none.
     */
    public Application getAcceptedApplication() {
        return acceptedApplication;
    }

    /**
     * Checks if the student is eligible to apply for a specific internship.
     * Rules: Year 1 and 2 students can only apply to BASIC level internships.
     * Year 3 and 4 students can apply to any level.
     *
     * @param internship The internship to check.
     * @return {@code true} if eligible, {@code false} otherwise.
     */
    public boolean canApplyTo(Internship internship) {
        if (internship == null) return false;

        // Y1-2 can only apply to BASIC; Y3-4 can apply to any level.
        switch (yearOfStudy) {
            case 1:
            case 2:
                return internship.getLevel() == InternshipLevel.BASIC;
            case 3:
            case 4:
            default:
                return true;
        }
    }

    /**
     * Submits a new application for the specified internship.
     * Validates that the student has not reached the limit of 3 active applications
     * and meets the eligibility criteria.
     *
     * @param internship The internship to apply for.
     * @return {@code true} if the application was created successfully; {@code false} if ineligible or quota reached.
     */
    public boolean applyFor(Internship internship) {
        if (internship == null) return false;
        if (getActiveApplicationCount() >= 3) return false;
        if (!canApplyTo(internship)) return false;

        Application app = new Application(this, internship, ApplicationStatus.PENDING);
        applications.add(app);
        return true;
    }

    /**
     * Requests a withdrawal for a specific application.
     *
     * @param app The application to withdraw.
     * @return {@code true} if the request was sent successfully; {@code false} if the application does not belong to this student.
     */
    public boolean withdraw(Application app) {
        if (app == null) return false;
        if (!applications.contains(app)) return false;
        app.requestWithdrawal();
        return true;
    }

    /**
     * Counts the number of currently active applications.
     * An active application is defined as PENDING and not yet WITHDRAWN.
     *
     * @return The count of active applications.
     */
    public int getActiveApplicationCount() {
        int cnt = 0;
        for (Application a : applications) {
            if (a == null) continue;
            if (a.getStatus() == ApplicationStatus.PENDING && !a.isWithdrawn()) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Accepts a successful internship offer.
     * If successful, this sets the accepted application and automatically withdraws
     * all other active applications.
     *
     * @param app The application to accept (must be SUCCESSFUL status).
     * @return {@code true} if the acceptance was processed; {@code false} if the application is invalid or not successful.
     */
    public boolean accept(Application app) {
        if (app == null) return false;

        // The application must belong to this student
        if (!applications.contains(app)) return false;

        if (hasAcceptedPlacement()) return false;

        if (app.getStatus() != ApplicationStatus.SUCCESSFUL || app.isWithdrawn()) {
            return false;
        }

        this.acceptedApplication = app;

        // Automatically withdraw all other active applications
        for (Application other : applications) {
            if (other == app) continue; // skip the one we just accepted
            if (other.isActive()) {
                other.withdrawDueToOtherAcceptance();
            }
        }

        return true;
    }

    /**
     * Retrieves a list of internships from the student's existing applications that remain eligible.
     *
     * @return A list of internships.
     */
    public List<Internship> viewEligibleInternships() {
        List<Internship> out = new ArrayList<>();
        for (Application a : applications) {
            Internship i = a.getInternship();
            if (i != null && canApplyTo(i)) {
                out.add(i);
            }
        }
        return out;
    }
}