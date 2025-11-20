package filter;

import entity.Internship;
import entity.Student;
import enums.InternshipStatus;

public class StudentFilter extends CompositeFilter<Internship> {

    private final Student student;

    public StudentFilter(Student student) {
        this.student = student;
        setDefaultFilters(); // apply mandatory defaults
    }

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
