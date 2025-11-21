package filter;

import entity.Internship;
import entity.Student;
import enums.InternshipStatus;

/**
 * A composite filter implementation specifically for Students.
 * This class establishes the default filtering rules for a student, ensuring they only see
 * internships that are approved, visible, and relevant to their specific major and year of study.
 */
public class StudentFilter extends CompositeFilter<Internship> {

    private final Student student;

    /**
     * Constructs a new filter configuration for a specific Student.
     * Initializes the filter with mandatory default settings (Visibility, Status, Major, Year).
     *
     * @param student The Student who is viewing the internship list.
     */
    public StudentFilter(Student student) {
        this.student = student;
        setDefaultFilters(); // apply mandatory defaults
    }

    /**
     * Resets and applies the default mandatory filters for a Student.
     * <p>
     * The default filters enforced are:
     * <ul>
     * <li>{@link VisibleFilter}: The internship must be marked as visible.</li>
     * <li>{@link StatusFilter}: The internship must be APPROVED.</li>
     * <li>{@link MajorFilter}: The internship must match the student's major.</li>
     * <li>{@link YearFilter}: The internship must match the student's year of study.</li>
     * </ul>
     * </p>
     */
    @Override
    protected void setDefaultFilters() {
        filters.clear();
        addFilter(new VisibleFilter());
        // Students should only see internships that are approved and visible
        addFilter(new StatusFilter(InternshipStatus.APPROVED));
        addFilter(new MajorFilter(student.getMajor()));
        addFilter(new YearFilter(student.getYearOfStudy()));
    }
}