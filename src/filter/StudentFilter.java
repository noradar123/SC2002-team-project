package filter;

import entity.Internship;
import entity.Student;

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
        addFilter(new MajorFilter(student.getMajor()));
        addFilter(new YearFilter(student.getYearOfStudy()));
    }
}
