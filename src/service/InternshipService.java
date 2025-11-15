package service;

import entity.Internship;
import java.util.List;
import java.util.Map;

public interface InternshipService {

    List<Internship> getAllInternships();

    Internship getInternshipById(String internshipId);

    List<Internship> filterInternships(Map<String, String> filters);

    List<Internship> getInternshipsByCompany(String companyId);

    boolean checkInternshipEligibility(String studentId, String internshipId);

    boolean updateInternshipStatus(String internshipId, String status);

    boolean toggleVisibility(String internshipId, boolean isVisible);
}
