package filter;

import entity.CompanyRep;
import entity.Internship;

/**
 * A composite filter implementation specifically for Company Representatives.
 * This class establishes the default filtering rules for a company rep, 
 * primarily restricting their view to internships owned by their own company.
 */
public class CompanyRepFilter extends CompositeFilter<Internship> {

    private final CompanyRep rep;

    /**
     * Constructs a new filter configuration for a specific Company Representative.
     * Initializes the filter and applies the default company restriction.
     *
     * @param rep The Company Representative whose view is being filtered.
     */
    public CompanyRepFilter(CompanyRep rep) {
        this.rep = rep;
        setDefaultFilters();
    }

    /**
     * Sets the default mandatory filters for the Company Representative.
     * <p>
     * By default, adds a {@link CompanyFilter} to ensure the representative 
     * can only see and manage internships belonging to their own company.
     * </p>
     */
    @Override
    protected void setDefaultFilters() {
        filters.clear();
        // CompanyRep sees only internships from their company by default
        addFilter(new CompanyFilter(rep.getCompany()));
    }
}