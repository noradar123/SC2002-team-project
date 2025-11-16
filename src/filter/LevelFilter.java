package filter;
import entity.Internship;
import enums.InternshipLevel;
public class LevelFilter implements Filter<Internship> {
    private final InternshipLevel level;
    public LevelFilter(InternshipLevel level) { this.level = level; }
    @Override
    public boolean matches(Internship i) { return i.getLevel() == level; }
}