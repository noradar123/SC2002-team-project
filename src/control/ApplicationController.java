package control;
import entity.Application;
import entity.Student;
import entity.Internship;
import data.ApplicationRepository;
import enums.ApplicationStatus;
import enums.InternshipLevel;
import enums.InternshipStatus;
import java.util.List;

public class ApplicationController {
	private ApplicationRepository applicationRepository;
	private static final int MAX_APPLICATIONS_PER_STUDENT = 3 ; 
	
	public ApplicationController(ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}
	
	// Getters // 
	public List<Application> getAllApplications() {
		return applicationRepository.findAll();
		}
	public Application getApplicationById(String applicationId) { 
		Application app = applicationRepository.findById(applicationId);
		if (app == null) {
			throw new IllegalArgumentException("Application not found: " + applicationId);
		}
		return app;
	}
	public List<Application> getApplicationByStudentId(String studentId){
		return applicationRepository.findByStudentId(studentId);
	}
	public List<Application> getApplicationByInternshipTitle(String internshipTitle){
		return applicationRepository.findByInternshipTitle(internshipTitle); 
	}
	public List<Application> getPendingWithdrawalRequest(){
		return applicationRepository.findPendingWithdrawalRequest();
	}
	
	// Create Operations for Applications// 
	public Application createApplication(Student student, Internship internship) {
		//check if student can apply how many applications (max 3) 
		int activeApplications = applicationRepository.countActiveApplicationsByStudent(student.getUserId());
		if (activeApplications >= MAX_APPLICATIONS_PER_STUDENT) {
			throw new IllegalArgumentException("Student has reached the maximum 3 active applications.");
		}
		
		// Check if student already has a successful application
		if(applicationRepository.hasSuccessfulApplication(student.getUserId())){
			throw new IllegalArgumentException("Student already has a successful application.");
		}
		
		// Year 1 and 2 can only apply for BASIC level Internship
		if (student.getYearOfStudy() <= 2 && internship.getLevel() != InternshipLevel.BASIC) {
			throw new IllegalArgumentException("Year 1 and 2 students are only eligible for Basic-Level Internship.");
		}
		//Check if internship is eligible for application 
		if (internship.getStatus() != InternshipStatus.APPROVED) {
			throw new IllegalArgumentException(" Internship is no longer eligible for application.");
		}
		//Check if internship is filled 
		if(internship.getStatus() == InternshipStatus.FILLED) {
			throw new IllegalArgumentException("Internship opportunity has been filled.");
		}
		//Check whether application deadline has passed 
		if (internship.isClosingDatePassed()) {
			throw new IllegalArgumentException("Application closing date has passed.");
		}
		
		//create and save application
		Application application = new Application(student, internship);
		return applicationRepository.save(application);
	}
	
	// Update Operations : called by ComRep
	public Application approveApplication(String applicationId) {
		Application application = getApplicationById(applicationId);
		// mark as successful 
		application.markSuccessful();
		return applicationRepository.update(application);
	}
	
	public Application rejectApplication(String applicationId) {
		Application application = getApplicationById(applicationId);
		//mark as unsuccessful 
		application.markUnsuccessful();
		return applicationRepository.update(application);
	}
	
	// Student Actions 
	public Application acceptPlacement(String applicationId) {
		Application application = getApplicationById(applicationId);
		// only can accept successful applications 
		if (application.getStatus()!= ApplicationStatus.SUCCESSFUL) {
			throw new IllegalStateException("Can only accept successful applications");
		}
		String studentId = application.getStudent().getUserId();
		//withdraw all other applications
		List<Application> otherApplications = applicationRepository.findByStudentId(studentId);
		for (Application otherApp : otherApplications) {
			if(!otherApp.getApplicationID().equals(applicationId) && !otherApp.isWithdrawn()) {
				otherApp.markWithDrawn();
				applicationRepository.update(otherApp);
			}
		}
		//Check if internship is now filled //
		Internship internship = application.getInternship();
		int confirmedSlots = applicationRepository.countSuccessfulApplicationByInternship(internship.getTitle());
		if (confirmedSlots >= internship.getSlots()) {
			internship.setStatus(InternshipStatus.FILLED);
		}
		
		return application;
	}
	
	public Application requestWithdrawal(String applicationId) {
		Application application = getApplicationById(applicationId);
		application.requestWithdrawal();
		return applicationRepository.update(application);
	}
	
	// Career Center Staff Actions 
	public Application approveWithdrawal(String applicationId) {
		Application application = getApplicationById(applicationId);
		if(!application.isWithdrawalRequested()) {
			throw new IllegalStateException("No withdrawal request foundfor this application");
		}
		application.markWithDrawn();
		return applicationRepository.update(application);
	}
	
	public Application rejectWithdrawal(String applicationId) {
		Application application = getApplicationById(applicationId);
		// Must have withdrawal request 
		if(!application.isWithdrawalRequested()) {
			throw new IllegalStateException("No withdrawal request found for this application");
		}
		//Clear withdrawal request flag 
		application.setWithdrawalRequested(false);
		return applicationRepository.update(application);
	}
	
	//delete application operation 
	public void deleteApplication (String applicationId) {
		Application application = getApplicationById(applicationId);
		// Can only delete pending application
		if(application.getStatus() != ApplicationStatus.PENDING) {
			throw new IllegalStateException("Can only delete pending application.");
		}
		applicationRepository.delete(applicationId);
	}

}
