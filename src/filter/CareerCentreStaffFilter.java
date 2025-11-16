package filter;

import entity.CareerCenterStaff;
import entity.Internship;

public class CareerCentreStaffFilter extends CompositeFilter<Internship> {

    private final CareerCenterStaff staff;

    public CareerCentreStaffFilter(CareerCenterStaff staff) {
        this.staff = staff;
        setDefaultFilters();
    }

    @Override
    protected void setDefaultFilters() {
        filters.clear();
        // Staff has no mandatory filters by default
        // They can choose any filter they want
        // can be used for future default filters if needed
    }
}
