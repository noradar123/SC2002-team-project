package filter;

import entity.Internship;
public class CompanyFilter implements Filter<Internship> {
    private final String company;
    public CompanyFilter(String company) { this.company = company; }
    @Override
    public boolean matches(Internship i) { return i.getCompany().equalsIgnoreCase(company); }
}