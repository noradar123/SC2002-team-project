package filter;
import entity.Internship;
import java.time.LocalDate;
public class CloseDateRangeFilter implements Filter<Internship> {
    private final LocalDate from, to;
    public CloseDateRangeFilter(LocalDate from, LocalDate to) { this.from = from; this.to = to; }
    @Override
    public boolean matches(Internship i) {
        if (from != null && i.getClosingDate().isBefore(from)) return false;
        if (to != null && i.getClosingDate().isAfter(to)) return false;
        return true;
    }
}
