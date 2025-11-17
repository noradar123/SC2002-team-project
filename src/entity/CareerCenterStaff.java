package entity;

import filter.CareerCentreStaffFilter;

public class CareerCenterStaff extends User {
    private String staffDepartment;

    public CareerCenterStaff(String userId, String name, String password, String staffDepartment) {
        super(userId, name, password);
        this.staffDepartment = staffDepartment;
        super.setFilter(new CareerCentreStaffFilter(this));
    }

    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}