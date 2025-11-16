package filter;

import entity.Internship;
public class YearFilter implements Filter<Internship> {
    private final int year;
    public YearFilter(int year) { this.year = year; }
    @Override
    public boolean matches(Internship i) { return i.isStudentYearEligible(year); } // so this one actually not filter by year 
    //but filter by intenrship level but the input is year. Maybe redundant, we can remove it later.
}
