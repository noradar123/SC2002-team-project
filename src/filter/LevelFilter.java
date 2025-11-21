package filter;

import entity.Internship;
import enums.InternshipLevel;

/**
 * A filter that selects internships based on their professional level.
 * It matches internships that correspond to a specific level (e.g., PROFESSIONAL, INTERNSHIP).
 */
public class LevelFilter implements Filter<Internship> {
    
    private final InternshipLevel level;

    /**
     * Constructs a new LevelFilter.
     *
     * @param level The {@link InternshipLevel} to filter by.
     */
    public LevelFilter(InternshipLevel level) { 
        this.level = level; 
    }

    @Override
    public boolean matches(Internship i) { 
        return i.getLevel() == level; 
    }
}