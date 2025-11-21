package data;

import entity.Application;
import enums.ApplicationStatus;
import java.util.ArrayList; 
import java.util.List; 
import java.util.stream.Collectors;

/**
 * Repository class responsible for the persistence and retrieval of Internship Applications.
 * This class manages the collection of applications, allowing for storing, updating, deleting,
 * and querying applications based on various criteria (Student, Internship, Status, etc.).
 */
public class ApplicationRepository {
	
	private List<Application> applications; 
	
	/**
	 * Constructs a new ApplicationRepository.
	 * Initializes the internal storage list.
	 */
	public ApplicationRepository() {
		this.applications = new ArrayList<>();
	}
	
	/**
	 * Saves a new application to the repository.
	 *
	 * @param application The application object to persist.
	 * @return The saved application object.
	 * @throws IllegalArgumentException If the application object is null.
	 */
	public Application save(Application application) {
		if(application == null) {
			throw new IllegalArgumentException("Application cannot be null");
		}
		applications.add(application);
		return application;
	}
	
	/**
	 * Retrieves all applications currently stored in the repository.
	 *
	 * @return A new list containing all applications (read-only copy).
	 */
	public List<Application> findAll(){
		return new ArrayList<>(applications);
	}
	
	/**
	 * Finds a specific application by its unique ID.
	 *
	 * @param applicationId The unique ID string.
	 * @return The {@link Application} object if found, or {@code null} otherwise.
	 */
	public Application findById(String applicationId) {
		return applications.stream()
				.filter(app->app.getApplicationID().equals(applicationId))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Retrieves all applications submitted by a specific student.
	 *
	 * @param studentId The ID of the student.
	 * @return A list of applications belonging to that student.
	 */
	public List<Application> findByStudentId(String studentId){
		return applications.stream()
				.filter(app -> app.getStudent().getUserId().equals(studentId))
				.collect(Collectors.toList());
		}
	
	/**
	 * Retrieves all applications associated with a specific internship title.
	 *
	 * @param internshipTitle The title of the internship.
	 * @return A list of applications for that internship.
	 */
	public List<Application> findByInternshipTitle (String internshipTitle){
        return applications.stream()
                .filter(app -> app.getInternship().getTitle().equals(internshipTitle))
                .collect(Collectors.toList());
    } 
	
	/**
	 * Retrieves all applications that currently match a specific status.
	 *
	 * @param status The {@link ApplicationStatus} to filter by.
	 * @return A list of matching applications.
	 */
	public List<Application> findByStatus(ApplicationStatus status){
		return applications.stream()
				.filter(app -> app.getStatus() == status)
				.collect(Collectors.toList());
	}
	
	/**
	 * Retrieves applications for a specific student that match a specific status.
	 *
	 * @param studentId The ID of the student.
	 * @param status    The status to filter by.
	 * @return A list of matching applications.
	 */
	public List<Application> findByStudentAndStatus(String studentId, ApplicationStatus status){
		return applications.stream()
				.filter(app -> app.getStudent().getUserId().equals(studentId)
						&& app.getStatus() == status) 
				.collect(Collectors.toList()); 
	}
	
	/**
	 * Finds all applications where a withdrawal has been requested but not yet finalized.
	 * Used by Career Center Staff to approve or reject withdrawals.
	 *
	 * @return A list of applications with pending withdrawal requests.
	 */
	public List<Application> findPendingWithdrawalRequest(){
		return applications.stream()
				.filter(app -> app.isWithdrawalRequested() && !app.isWithdrawn())
				.collect(Collectors.toList());
	}

	/**
	 * Updates an existing application in the repository.
	 * Finds the application by ID and replaces it with the new object.
	 *
	 * @param application The application with updated data.
	 * @return The updated application object.
	 * @throws IllegalArgumentException If the application is null or does not exist.
	 */
	public Application update(Application application) {
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null."); 
		}
		
		Application existing = findById(application.getApplicationID());
		if (existing == null) {
			throw new IllegalArgumentException("Application not found : " + application.getApplicationID());
		}
		
		//update here
		int index = applications.indexOf(existing);
		applications.set(index, application);
		return application;	
	}

	/**
	 * Deletes an application from the repository.
	 *
	 * @param applicationId The unique ID of the application to remove.
	 * @return {@code true} if found and deleted; {@code false} otherwise.
	 */
	public boolean delete(String applicationId) {
		Application app = findById(applicationId);
		if(app != null) {
			applications.remove(app);
			return true;
		}
		return false;
	}
	
	// ========== UTILITY METHODS ==========
	
	/**
	 * Counts the number of active applications for a specific student.
	 * An application is considered active if it is not withdrawn and not unsuccessful.
	 * This is used to enforce the limit of 3 active applications.
	 *
	 * @param studentId The ID of the student.
	 * @return The count of active applications.
	 */
	public int countActiveApplicationsByStudent(String studentId) {
        return (int) applications.stream()
                .filter(app -> app.getStudent().getUserId().equals(studentId))
                .filter(app -> !app.isWithdrawn() 
                        && app.getStatus() != ApplicationStatus.UNSUCCESSFUL)
                .count();
    }
	
	/**
	 * Counts the number of successful applications for a specific internship.
	 * This is used to calculate how many slots have been filled.
	 *
	 * @param internshipTitle The title of the internship.
	 * @return The count of successful applications.
	 */
	public int countSuccessfulApplicationByInternship(String internshipTitle) {
		return (int)applications.stream()
				.filter(app -> app.getInternship().getTitle().equals(internshipTitle))
				.filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
				.count();
	}
	
	/**
	 * Checks if a student has already secured an internship (has a SUCCESSFUL application).
	 *
	 * @param studentId The ID of the student.
	 * @return {@code true} if the student has an accepted/successful application; {@code false} otherwise.
	 */
	public boolean hasSuccessfulApplication(String studentId) { 
		return applications.stream()
				.filter(app-> app.getStudent().getUserId().equals(studentId))
				.anyMatch(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL);
	}
}