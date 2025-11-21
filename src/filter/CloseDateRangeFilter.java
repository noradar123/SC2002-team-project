package filter;
import entity.Internship;
import java.time.LocalDate;
/**
 * A filter that selects internships based on their closing date.
 * It checks if the internship's closing date falls within the specified start and end dates (inclusive).
 */
public class CloseDateRangeFilter implements Filter<Internship> {
    private final LocalDate from, to;
    /**
     * Constructs a new CloseDateRangeFilter.
     *
     * @param from The start date of the range (inclusive). If null, there is no lower bound.
     * @param to   The end date of the range (inclusive). If null, there is no upper bound.
     */
    public CloseDateRangeFilter(LocalDate from, LocalDate to) { this.from = from; this.to = to; }
    @Override
    public boolean matches(Internship i) {
        if (from != null && i.getClosingDate().isBefore(from)) return false;
        if (to != null && i.getClosingDate().isAfter(to)) return false;
        return true;
    }
}
