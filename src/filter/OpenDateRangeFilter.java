package filter;
import entity.Internship;
import java.time.LocalDate;
public class OpenDateRangeFilter implements Filter<Internship> {
    private final LocalDate from, to;
    public OpenDateRangeFilter(LocalDate from, LocalDate to) { this.from = from; this.to = to; }
    @Override
    public boolean matches(Internship i) {
        if (from != null && i.getOpenDate().isBefore(from)) return false;
        if (to != null && i.getOpenDate().isAfter(to)) return false;
        return true;
    }
}