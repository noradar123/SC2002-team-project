package filter;
import entity.Internship;

public class VisibleFilter implements Filter<Internship> {
    @Override
    public boolean matches(Internship i) {
        return i.isVisible();
    }
}