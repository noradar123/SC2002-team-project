package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enums.ApplicationStatus;

public class Student extends User {
    private final int yearOfStudy;                 
    private final String major;                    
    private final List<Application> applications;  // up to 3 concurrent
    private Application acceptedApplication;       // 0..1

    public Student(String userId, String name, String password,
                    int yearOfStudy, String major) {
        super(userId, name, password);
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
    }
    
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<Application> getAppliedInternships() {
        return Collections.unmodifiableList(applications);
    }

    public boolean hasAcceptedPlacement() {
        return acceptedApplication != null;
    }

    public Application getAcceptedApplication() {
        return acceptedApplication;
    }

    public boolean canApplyTo(Internship internship) {
        if (internship == null) return false;

        // Y1-2 can only apply to BASIC; Y3-4 can apply to any level.
        switch (yearOfStudy) {
            case 1:
            case 2:
                return internship.getLevel() == enums.InternshipLevel.BASIC;
            case 3:
            case 4:
            default:
                return true;
        }
    }

     public boolean applyFor(Internship internship) {
        if (internship == null) return false;
        if (getActiveApplicationCount() >= 3) return false;
        if (!canApplyTo(internship)) return false;

        Application app = new Application(this, internship, ApplicationStatus.PENDING);
        applications.add(app);
        return true;
    }

    public boolean withdraw(Application app) {
        if (app == null) return false;
        if (!applications.contains(app)) return false;
        app.requestWithdrawal();
        return true;
    }
    
    public int getActiveApplicationCount() {
        int cnt = 0;
        for (Application a : applications) {
            if (a == null) continue;
            if (a.getStatus() == enums.ApplicationStatus.PENDING && !a.isWithdrawn()) {
                cnt++;
            }
        }
        return cnt;
    }

    public boolean accept(Application app) {
        if (app == null) return false;
        if (hasAcceptedPlacement()) return false;
        if (!applications.contains(app)) return false;
        if (app.getStatus() != ApplicationStatus.SUCCESSFUL) return false;

        this.acceptedApplication = app;
        return true;
    }

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
