package filter;

import entity.Internship;

/**
 * A filter that determines if an internship is suitable for a student of a specific year.
 * Instead of a direct equality check, this filter delegates to the internship's eligibility logic 
 * (e.g., mapping the student's year to the required Internship Level).
 */
public class YearFilter implements Filter<Internship> {
    
    private final int year;

    /**
     * Constructs a new YearFilter.
     *
     * @param year The student's year of study (e.g., 1, 2, 3) to check eligibility for.
     */
    public YearFilter(int year) { 
        this.year = year; 
    }

    @Override
    public boolean matches(Internship i) { 
        return i.isStudentYearEligible(year); 
    } 
}