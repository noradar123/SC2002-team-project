package data;

import entity.Internship;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InternshipRepository {
    private final List<Internship> InternshipList = new ArrayList<>();

    public void add(Internship i) { InternshipList.add(i); }
    public boolean remove(Internship i) { return InternshipList.remove(i); }
    public List<Internship> all() { return new ArrayList<>(InternshipList); }

    public List<Internship> findByCompany(String company) {
        return InternshipList.stream()
                .filter(i -> i.getCompany().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }
    public List<Internship> findByLevel(InternshipLevel level) {
        return InternshipList.stream()
                .filter(i -> i.getLevel() == level)
                .collect(Collectors.toList());
    }
    public List<Internship> findByStatus(InternshipStatus status) {
        return InternshipList.stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }
    public List<Internship> findVisible() {
        return InternshipList.stream()
                .filter(Internship::isVisible)
                .collect(Collectors.toList());
    }
    public List<Internship> findByMajor(String major) {
        return InternshipList.stream()
                .filter(i -> i.getMajors().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }

}
