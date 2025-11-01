package entity;

public class CompanyRep extends User {
    private boolean approved;
    private final String company;
    private final String department;
    private final String position;
    private int numberOfInternships;

    public CompanyRep(String userId, String name, String password,
                      String company, String department, String position) {
        super(userId, name, password);
        this.company = company;
        this.department = department;
        this.position = position;
        this.approved = false;
        this.numberOfInternships = 0;
    }

    public boolean isApproved() { return approved; }
    public void approveByCCS() { this.approved = true; }

    public boolean loginIfApproved(String password) { return approved && login(password); }

    public String getCompany() { return company; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }

    public int getNumberOfInternships() { return numberOfInternships; }
    public void incrementInternships() { numberOfInternships++; }
    public void decrementInternships() { if (numberOfInternships > 0) numberOfInternships--; }

    @Override
    public String toString() {
        return String.format("CompanyRep[userId=%s, name=%s, company=%s, dept=%s, position=%s, approved=%b, internships=%d]",
                getUserId(), getName(), company, department, position, approved, numberOfInternships);
    }
}
