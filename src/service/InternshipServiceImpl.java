package service;

import entity.Internship;
import store.InternshipRepository;
import java.util.List;
import java.util.Map;

public class InternshipServiceImpl implements InternshipService {

    private InternshipRepository internshipRepository = new InternshipRepository();
    public InternshipServiceImpl(InternshipRepository repo) {
        this.internshipRepository = repo;
    }

    @Override
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    @Override
    public Internship getInternshipById(String internshipId) {
        return internshipRepository.findById(internshipId);
    }

    @Override
    public List<Internship> filterInternships(Map<String, String> filters) {
        // Call repository to get filtered internships
        String status = filters.get("status");
        String level = filters.get("level");
        String major = filters.get("major");
        return internshipRepository.findByFilter(status, level, major);  // Call the filtering method in the repository
    }

    @Override
    public List<Internship> getInternshipsByCompany(String companyId) {
        return internshipRepository.findByCompany(companyId);
    }

    @Override
    public boolean checkInternshipEligibility(String studentId, String internshipId) {
        return internshipRepository.checkEligibility(studentId, internshipId);
    }

    @Override
    public boolean updateInternshipStatus(String internshipId, String status) {
        return internshipRepository.updateStatus(internshipId, status);
    }

    @Override
    public boolean toggleVisibility(String internshipId, boolean isVisible) {
        return internshipRepository.toggleVisibility(internshipId, isVisible);
    }
}
