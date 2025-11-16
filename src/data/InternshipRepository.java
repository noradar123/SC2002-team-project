package data;

import entity.Internship;
import java.util.ArrayList;
import java.util.List;

public class InternshipRepository {
    private final List<Internship> internships = new ArrayList<>();

    // Add new internship
    public void add(Internship internship) {
        if (internship == null) throw new IllegalArgumentException("Internship cannot be null");
        internships.add(internship);
    }

    // Remove by ID
    public boolean remove(String id) {
        return internships.removeIf(i -> i.getId().equals(id)); // remember later must check if false, i.e., not found, then flag
    }

    // Get all internships
    public List<Internship> all() {
        return new ArrayList<>(internships);
    }

    // Find by ID
    public Internship findById(String id) {
        return internships.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update by replacing fields
    public Internship update(Internship updated) {
        Internship existing = findById(updated.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Internship not found: " + updated.getId());
        }
        existing.updateFrom(updated);
        return existing; //I return the updated internship so that the view or controller can use it if needed. I forget who is doing it, please take note of this.
        // to use just like this: Internship updatedInternship = repo.update(updatedInternship);
    }
}
