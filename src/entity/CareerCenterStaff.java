package entity;

import filter.CareerCentreStaffFilter;

/**
 * Represents a Career Center Staff member user in the system.
 * Staff members have administrative privileges, including the ability to approve company representatives
 * and generate reports.
 */
public class CareerCenterStaff extends User {
    
    private String staffDepartment;

    /**
     * Constructs a new Career Center Staff member.
     * Initializes the user with specific staff credentials and assigns a default
     * {@link CareerCentreStaffFilter} to their session.
     *
     * @param userId          The unique user ID (e.g., email or staff ID).
     * @param name            The full name of the staff member.
     * @param password        The login password.
     * @param staffDepartment The specific department the staff member belongs to.
     */
    public CareerCenterStaff(String userId, String name, String password, String staffDepartment) {
        super(userId, name, password);
        this.staffDepartment = staffDepartment;
        super.setFilter(new CareerCentreStaffFilter(this));
    }

    /**
     * Retrieves the department name of the staff member.
     *
     * @return The staff department.
     */
    public String getStaffDepartment() {
        return staffDepartment;
    }

    /**
     * Updates the department name for the staff member.
     *
     * @param staffDepartment The new department name.
     */
    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}