package filter;

import entity.Internship;

/**
 * A filter that selects internships that are currently marked as visible.
 * This is typically used to hide closed or filled internships from students.
 */
public class VisibleFilter implements Filter<Internship> {

    /**
     * Constructs a new VisibleFilter.
     */
    public VisibleFilter() {
        // Default constructor
    }

    @Override
    public boolean matches(Internship i) {
        return i.isVisible();
    }
}