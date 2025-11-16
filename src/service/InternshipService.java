package service;

import data.InternshipRepository;
import data.ApplicationRepository;
import entity.Internship;
import entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class InternshipService {

    private final InternshipRepository repo;

    public InternshipService(InternshipRepository repo) {
        this.repo = repo;
    }

    // return a list of internships based on the user filter settings
    public List<Internship> getInternshipsFor(User user) {
    	return repo.all().stream()
    	        .filter(i -> user.getFilter().matches(i))
    	        .collect(Collectors.toList());
    }

	// Edit internship if status is PENDING
    
    public void editInternship(String id, Internship updated) {
        Internship existing = repo.findById(id);
        if (existing == null) throw new IllegalArgumentException("Internship not found: " + id);
        if (existing.getStatus() != null && existing.getStatus().name().equals("PENDING")) {
            repo.update(updated);
        } else {
            throw new IllegalStateException("Cannot edit internship unless status is PENDING");
        }
    }
    // Mark as filled
    public void markAsFilled(Internship internship) {
        if (internship.getFilledSlots() >= internship.getSlots()) {
            internship.setVisible(false);
            internship.setStatus(enums.InternshipStatus.FILLED);
        }
    }
}
