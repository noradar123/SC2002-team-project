package filter;

import entity.Internship;

/**
 * A filter that selects internships based on the preferred major.
 * It performs a case-insensitive comparison to match the internship's preferred major field.
 */
public class MajorFilter implements Filter<Internship> {
    
    private final String major;

    /**
     * Constructs a new MajorFilter.
     *
     * @param major The name of the major to filter for (e.g., "Computer Science").
     */
    public MajorFilter(String major) { 
        this.major = major; 
    }

    @Override
    public boolean matches(Internship i) { 
        return i.getPreferredMajor().equalsIgnoreCase(major); 
    }
}