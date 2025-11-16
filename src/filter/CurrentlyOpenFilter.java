package filter;
import entity.Internship;
import java.time.LocalDate;
public class CurrentlyOpenFilter implements Filter<Internship> {
    @Override
    public boolean matches(Internship i) {
        LocalDate now = LocalDate.now();
        return !now.isBefore(i.getOpenDate()) && !now.isAfter(i.getClosingDate());
    }
}
