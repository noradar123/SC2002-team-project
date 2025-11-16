package filter;

import entity.CompanyRep;
import entity.Internship;

public class CompanyRepFilter extends CompositeFilter<Internship> {

    private final CompanyRep rep;

    public CompanyRepFilter(CompanyRep rep) {
        this.rep = rep;
        setDefaultFilters();
    }

    @Override
    protected void setDefaultFilters() {
        filters.clear();
        // CompanyRep sees only internships from their company by default
        addFilter(new CompanyFilter(rep.getCompany()));
        
    }
}
