package filter;

import entity.Internship;

public class MajorFilter implements Filter<Internship> {
    private final String major;
    public MajorFilter(String major) { this.major = major; }
    @Override
    public boolean matches(Internship i) { return i.getPreferredMajor().equalsIgnoreCase(major); }
}
