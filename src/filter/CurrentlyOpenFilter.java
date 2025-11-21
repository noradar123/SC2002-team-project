package filter;

import entity.Internship;
import java.time.LocalDate;

/**
 * A filter that selects internships that are currently active.
 * It checks if the current system date falls within the internship's application period 
 * (on or after the Open Date and on or before the Closing Date).
 */
public class CurrentlyOpenFilter implements Filter<Internship> {

    /**
     * Constructs a new CurrentlyOpenFilter.
     */
    public CurrentlyOpenFilter() {
        // Default constructor
    }

    @Override
    public boolean matches(Internship i) {
        LocalDate now = LocalDate.now();
        return !now.isBefore(i.getOpenDate()) && !now.isAfter(i.getClosingDate());
    }
}