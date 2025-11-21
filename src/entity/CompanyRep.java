package entity;

import filter.CompanyRepFilter;

/**
 * Represents a Company Representative user.
 * This user is responsible for creating and managing internship listings for their specific company.
 * They utilize a specific {@link CompanyRepFilter} to restrict their view to their own company's data.
 */
public class CompanyRep extends User {
    private boolean approved;
    private final String company;
    private final String department;
    private final String position;
    private int numberOfInternships;
    private boolean authorized;

    /**
     * Constructs a new Company Representative.
     * Initializes the user as unapproved and unauthorized by default, and assigns a
     * {@link CompanyRepFilter} to ensure they only see their own company's internships.
     *
     * @param userId     The unique user ID (email).
     * @param name       The full name of the representative.
     * @param password   The login password.
     * @param company    The name of the company the representative belongs to.
     * @param department The department within the company.
     * @param position   The job title/position of the representative.
     */
    public CompanyRep(String userId, String name, String password,
                      String company, String department, String position) {
        super(userId, name, password);
        this.company = company;
        this.department = department;
        this.position = position;
        this.approved = false; // Default requires admin approval
        this.numberOfInternships = 0;
        this.authorized = false;
        
        CompanyRepFilter filter = new CompanyRepFilter(this);
        super.setFilter(filter);
    }

    /**
     * Checks if the representative's account creation has been approved by Career Center Staff.
     * @return true if approved, false otherwise.
     */
    public boolean isApproved() { return approved; }

    /**
     * Marks the representative as approved.
     * This is typically called by a Career Center Staff member.
     */
    public void approveByCCS() { this.approved = true; }

    /**
     * Retrieves the company name.
     * @return The company name.
     */
    public String getCompany() { return company; }

    /**
     * Retrieves the department name.
     * @return The department.
     */
    public String getDepartment() { return department; }

    /**
     * Retrieves the job title/position.
     * @return The position.
     */
    public String getPosition() { return position; }

    /**
     * Checks if the user is currently authorized to access the system (e.g., login allowed).
     * @return true if authorized.
     */
    public boolean isAuthorized() {return authorized;}

    /**
     * Retrieves the current number of internships posted by this representative.
     * Used to enforce the posting quota (typically max 5).
     * @return The count of internships.
     */
    public int getNumberOfInternships() { return numberOfInternships; }

    /**
     * Increments the count of internships posted.
     */
    public void incrementInternships() { numberOfInternships++; }

    /**
     * Decrements the count of internships posted.
     * Used when an internship is deleted.
     */
    public void decrementInternships() { if (numberOfInternships > 0) numberOfInternships--; }

    /**
     * Sets the authorization status of the representative.
     * @param authorized true to grant access, false to revoke.
     */
    public void setAuthorized(boolean authorized) {
    	this.authorized = authorized;
    }
}