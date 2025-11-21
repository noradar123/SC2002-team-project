package filter;

import entity.CareerCenterStaff;
import entity.Internship;

/**
 * A composite filter implementation specifically for Career Center Staff.
 * This class defines the default filtering rules applied when a staff member views internships.
 * Currently, allows for an unrestricted view but can be extended to enforce specific defaults.
 */
public class CareerCentreStaffFilter extends CompositeFilter<Internship> {

    private final CareerCenterStaff staff;

    /**
     * Constructs a new filter configuration for a specific Career Center Staff member.
     * Initializes the filter with default settings suitable for staff access.
     *
     * @param staff The Career Center Staff member accessing the list.
     */
    public CareerCentreStaffFilter(CareerCenterStaff staff) {
        this.staff = staff;
        setDefaultFilters();
    }

    /**
     * Sets the default filters for the staff member.
     * <p>
     * currently, this clears all filters to ensure Staff can see everything by default.
     * Future mandatory filters for staff can be added here.
     * </p>
     */
    @Override
    protected void setDefaultFilters() {
        filters.clear();
        // Staff has no mandatory filters by default
        // They can choose any filter they want
        // can be used for future default filters if needed
    }
}