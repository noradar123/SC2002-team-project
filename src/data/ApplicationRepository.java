package data;

import entity.Application;
import enums.ApplicationStatus;
import java.util.ArrayList; 
import java.util.List; 
import java.util.stream.Collectors;

public class ApplicationRepository {
	private List<Application> applications; 
	
	public ApplicationRepository() {
		this.applications = new ArrayList<>();
	}
	
	//Save this to database // 
	public Application save(Application application) {
		if(application == null) {
			throw new IllegalArgumentException("Application cannot be null");
		}
		applications.add(application);
		return application;
	}
	
	//Read only// 
	public List<Application> findAll(){
		return new ArrayList<>(applications);
	}
	
	public Application findById(String applicationId) {
		return applications.stream()
				.filter(app->app.getApplicationID().equals(applicationId))
				.findFirst()
				.orElse(null);
	}
	
	public List<Application> findByStudentId(String studentId){
		return applications.stream()
				.filter(app -> app.getStudent().getUserId().equals(studentId))
				.collect(Collectors.toList());
		}
	
	public List<Application> findByInternshipTitle (String internshipTitle){
        return applications.stream()
                .filter(app -> app.getInternship().getTitle().equals(internshipTitle))
                .collect(Collectors.toList());
    } 
	
	public List<Application> findByStatus(ApplicationStatus status){
		return applications.stream()
				.filter(app -> app.getStatus() == status)
				.collect(Collectors.toList());
	}
	
	public List<Application> findByStudentAndStatus(String studentId, ApplicationStatus status){
		return applications.stream()
				.filter(app -> app.getStudent().getUserId().equals(studentId)
						&& app.getStatus() == status) 
				.collect(Collectors.toList()); 
	}
	
	public List<Application> findPendingWithdrawalRequest(){
		return applications.stream()
				.filter(app -> app.isWithdrawalRequested() && !app.isWithdrawn())
				.collect(Collectors.toList());
	}
	//Update the list // 
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
	//delete 
	public boolean delete(String applicationId) {
		Application app = findById(applicationId);
		if(app != null) {
			applications.remove(app);
			return true;
		}
		return false;
	}
	
	//Utility//
	
	public int countActiveApplicationsByStudent(String studentId) {
        return (int) applications.stream()
                .filter(app -> app.getStudent().getUserId().equals(studentId))
                .filter(app -> !app.isWithdrawn() 
                        && app.getStatus() != ApplicationStatus.UNSUCCESSFUL)
                .count();
    }
	
	public int countSuccessfulApplicationByInternship(String internshipTitle) {
		return (int)applications.stream()
				.filter(app -> app.getInternship().getTitle().equals(internshipTitle))
				.filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
				.count();
	}
	
	
	
	public boolean hasSuccessfulApplication(String studentId) { 
		return applications.stream()
				.filter(app-> app.getStudent().getUserId().equals(studentId))
				.anyMatch(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL);
		}
	
	
	}


