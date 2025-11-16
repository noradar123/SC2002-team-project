package filter;
import entity.Internship;
import enums.InternshipStatus;
public class StatusFilter implements Filter<Internship> {
    private final InternshipStatus status;
    public StatusFilter(InternshipStatus status) { this.status = status; }
    @Override
    public boolean matches(Internship i) { return i.getStatus() == status; }
}
