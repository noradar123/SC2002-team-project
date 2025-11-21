package filter;

import entity.Internship;
import enums.InternshipStatus;

/**
 * A filter that selects internships based on their current status.
 * It matches internships that correspond to a specific status (e.g., PENDING, APPROVED, FILLED).
 */
public class StatusFilter implements Filter<Internship> {
    
    private final InternshipStatus status;

    /**
     * Constructs a new StatusFilter.
     *
     * @param status The {@link InternshipStatus} to filter by.
     */
    public StatusFilter(InternshipStatus status) { 
        this.status = status; 
    }

    @Override
    public boolean matches(Internship i) { 
        return i.getStatus() == status; 
    }
}