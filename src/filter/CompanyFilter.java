package filter;

import entity.Internship;

/**
 * A filter that selects internships based on the company name.
 * It performs a case-insensitive comparison to match the internship's company field.
 */
public class CompanyFilter implements Filter<Internship> {
    
    private final String company;

    /**
     * Constructs a new CompanyFilter.
     *
     * @param company The name of the company to filter for (e.g., "Google", "Shopee").
     */
    public CompanyFilter(String company) { 
        this.company = company; 
    }

    @Override
    public boolean matches(Internship i) { 
        return i.getCompany().equalsIgnoreCase(company); 
    }
}