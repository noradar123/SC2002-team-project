package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import enums.ApplicationStatus;

public class Application {
	
	private static AtomicInteger idCounter = new AtomicInteger(1);
	private String applicationID;
	private Student student; 
	private Internship internship;
	private ApplicationStatus status;
	private LocalDate applicationDate;
	private boolean withdrawalRequested; 
	private boolean withdrawn;
	
	public Application(Student student, Internship internship) {
		this.applicationID = generateApplicationID(); 
		this.student = student; 
		this.internship = internship; 
		this.status = ApplicationStatus.PENDING;
		this.applicationDate = LocalDate.now();
		this.withdrawalRequested = false;
		this.withdrawn = false;
	}
	public Application(Student student, Internship internship, ApplicationStatus status) {
		this.applicationID = generateApplicationID();
		this.student = student;
		this.internship = internship;
		this.status = status;
		this.applicationDate = LocalDate.now();
		this.withdrawalRequested = false;
		this.withdrawn = false;
	}
	// Generating applicationID -- returning something like: APP-YYYYMMDD-#### //
	public String generateApplicationID() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateStr = LocalDate.now().format(formatter);
		int id = idCounter.getAndIncrement();
		return String.format("APP-%s-%04d",dateStr,id);
	}
	
	//getters//
	public String getApplicationID() {return applicationID;}
	public Student getStudent() {return student;}
	public Internship getInternship() {return internship;}
	public ApplicationStatus getStatus() {return status;}
	public LocalDate getApplicationDate() {return applicationDate;}
	public boolean isWithdrawalRequested() {return withdrawalRequested;}
	public boolean isWithdrawn() {return withdrawn;}
	
	//setter//
	public void setWithdrawalRequested(boolean withdrawalRequested) {
		this.withdrawalRequested = withdrawalRequested;
	}
	
	// Called by ComRep when approving/ rejection an application // 
	public void markSuccessful() {
		if (this.status == ApplicationStatus.PENDING && !this.withdrawn) {
			this.status = ApplicationStatus.SUCCESSFUL;
		}else {
			throw new IllegalStateException("Mark as succesful is failed; Application must be pending and cannot be withdrawn.");
		}
	}
	
	public void markUnsuccessful() {
		if(this.status == ApplicationStatus.PENDING && !this.withdrawn) {
			this.status = ApplicationStatus.UNSUCCESSFUL;
		}else {
			throw new IllegalStateException("Mark as unsuccessul is failed; Application must be pending and cannot be withdrawn.");
		}
	}
	
	// Check whether application can be withdrawn// 
	public boolean canBeWithdrawn(){
		// already withdrawn//
		if(this.withdrawn) {
			return false;
		}
		// cannot withdraw as application unsuccessful//
		if (this.status == ApplicationStatus.UNSUCCESSFUL) {
			return false;
		}
		// can be withdraw as application pending / successful//
		// Note: Successful Application need approval from career center //
		return(this.status == ApplicationStatus.PENDING || this.status == ApplicationStatus.SUCCESSFUL);
	}
		
	// Student request to withdraw application// 
	// Flag for Career Center Staff approval //
	public void requestWithdrawal() {
		if(canBeWithdrawn()) {
			this.withdrawalRequested = true;
		}else {
			throw new IllegalStateException("Application cannot be withdrawn in its current state.");
		}
	}
	
	/*Called by Career Center Staff after approving withdrawal request or 
	automatically when student accepts another placement*/
	public void markWithDrawn() {
		this.withdrawn = true ;
		this.status = ApplicationStatus.WITHDRAWN;
	}
	
	@Override
    public String toString() {
        return String.format("Application[ID=%s, Student=%s, Internship=%s, Status=%s, Date=%s, WithdrawalRequested=%b, Withdrawn=%b]",
            applicationID, student.getUserId(), internship.getTitle(), status, 
            applicationDate, withdrawalRequested, withdrawn);
    }
	
}
