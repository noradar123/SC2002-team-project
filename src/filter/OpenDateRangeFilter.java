package filter;

import entity.Internship;
import java.time.LocalDate;

/**
 * A filter that selects internships based on their opening date.
 * It checks if the internship's open date falls within the specified start and end dates (inclusive).
 */
public class OpenDateRangeFilter implements Filter<Internship> {
    
    private final LocalDate from, to;

    /**
     * Constructs a new OpenDateRangeFilter.
     *
     * @param from The start date of the range (inclusive). If null, there is no lower bound.
     * @param to   The end date of the range (inclusive). If null, there is no upper bound.
     */
    public OpenDateRangeFilter(LocalDate from, LocalDate to) { 
        this.from = from; 
        this.to = to; 
    }

    @Override
    public boolean matches(Internship i) {
        if (from != null && i.getOpenDate().isBefore(from)) return false;
        if (to != null && i.getOpenDate().isAfter(to)) return false;
        return true;
    }
}