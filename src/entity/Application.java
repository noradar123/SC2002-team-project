package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import enums.ApplicationStatus;

/**
 * Represents a formal application made by a Student for a specific Internship.
 * Tracks the unique ID, current status, dates, and withdrawal requests associated with the application.
 */
public class Application {
	
	private static AtomicInteger idCounter = new AtomicInteger(1);
	private String applicationID;
	private Student student; 
	private Internship internship;
	private ApplicationStatus status;
	private LocalDate applicationDate;
	private boolean withdrawalRequested; 
	private boolean withdrawn;
	
	/**
	 * Constructs a new Application with a default PENDING status.
	 * Generates a unique ID based on the current date.
	 *
	 * @param student    The student applying.
	 * @param internship The internship being applied for.
	 */
	public Application(Student student, Internship internship) {
		this.applicationID = generateApplicationID(); 
		this.student = student; 
		this.internship = internship; 
		this.status = ApplicationStatus.PENDING;
		this.applicationDate = LocalDate.now();
		this.withdrawalRequested = false;
		this.withdrawn = false;
	}

	/**
	 * Constructs a new Application with a specific initial status.
	 *
	 * @param student    The student applying.
	 * @param internship The internship being applied for.
	 * @param status     The initial status of the application.
	 */
	public Application(Student student, Internship internship, ApplicationStatus status) {
		this.applicationID = generateApplicationID();
		this.student = student;
		this.internship = internship;
		this.status = status;
		this.applicationDate = LocalDate.now();
		this.withdrawalRequested = false;
		this.withdrawn = false;
	}

	/**
	 * Generates a unique application ID in the format APP-YYYYMMDD-####.
	 * Uses an atomic counter to ensure uniqueness within the session.
	 *
	 * @return A formatted string ID.
	 */
	public String generateApplicationID() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateStr = LocalDate.now().format(formatter);
		int id = idCounter.getAndIncrement();
		return String.format("APP-%s-%04d", dateStr, id);
	}
	
	// ========== GETTERS ==========

	/**
	 * Gets the unique application ID.
	 * @return The application ID string.
	 */
	public String getApplicationID() { return applicationID; }

	/**
	 * Gets the student who submitted the application.
	 * @return The Student object.
	 */
	public Student getStudent() { return student; }

	/**
	 * Gets the internship associated with this application.
	 * @return The Internship object.
	 */
	public Internship getInternship() { return internship; }

	/**
	 * Gets the current status of the application.
	 * @return The ApplicationStatus enum.
	 */
	public ApplicationStatus getStatus() { return status; }

	/**
	 * Gets the date the application was created.
	 * @return The creation date.
	 */
	public LocalDate getApplicationDate() { return applicationDate; }

	/**
	 * Checks if a withdrawal has been requested by the student.
	 * @return true if withdrawal is pending approval, false otherwise.
	 */
	public boolean isWithdrawalRequested() { return withdrawalRequested; }

	/**
	 * Checks if the application has been finalized as withdrawn.
	 * @return true if the application is withdrawn.
	 */
	public boolean isWithdrawn() { return withdrawn; }
	
	// ========== SETTERS & LOGIC ==========

	/**
	 * Sets the withdrawal requested flag.
	 *
	 * @param withdrawalRequested true to signal a request for withdrawal.
	 */
	public void setWithdrawalRequested(boolean withdrawalRequested) {
		this.withdrawalRequested = withdrawalRequested;
	}
	
	/**
	 * Marks the application as SUCCESSFUL (Approved).
	 * Typically called by a Company Representative.
	 *
	 * @throws IllegalStateException If the application is not PENDING or has already been withdrawn.
	 */
	public void markSuccessful() {
		if (this.status == ApplicationStatus.PENDING && !this.withdrawn) {
			this.status = ApplicationStatus.SUCCESSFUL;
		} else {
			throw new IllegalStateException("Mark as successful failed; Application must be pending and cannot be withdrawn.");
		}
	}
	
	/**
	 * Marks the application as UNSUCCESSFUL (Rejected).
	 * Typically called by a Company Representative.
	 *
	 * @throws IllegalStateException If the application is not PENDING or has already been withdrawn.
	 */
	public void markUnsuccessful() {
		if (this.status == ApplicationStatus.PENDING && !this.withdrawn) {
			this.status = ApplicationStatus.UNSUCCESSFUL;
		} else {
			throw new IllegalStateException("Mark as unsuccessful failed; Application must be pending and cannot be withdrawn.");
		}
	}
	
	/**
	 * Determines if the application is eligible to be withdrawn.
	 * An application can be withdrawn if it is PENDING or SUCCESSFUL (with approval),
	 * provided it hasn't been withdrawn already or rejected.
	 *
	 * @return true if eligible for withdrawal.
	 */
	public boolean canBeWithdrawn() {
		// already withdrawn
		if (this.withdrawn) {
			return false;
		}
		// cannot withdraw as application unsuccessful
		if (this.status == ApplicationStatus.UNSUCCESSFUL) {
			return false;
		}
		// can be withdrawn as application pending / successful
		// Note: Successful Application needs approval from career center
		return (this.status == ApplicationStatus.PENDING || this.status == ApplicationStatus.SUCCESSFUL);
	}
		
	/**
	 * Flags the application for withdrawal.
	 * This indicates that the student wishes to withdraw, but requires Career Center Staff approval.
	 *
	 * @throws IllegalStateException If the application cannot be withdrawn in its current state.
	 */
	public void requestWithdrawal() {
		if (canBeWithdrawn()) {
			this.withdrawalRequested = true;
		} else {
			throw new IllegalStateException("Application cannot be withdrawn in its current state.");
		}
	}

	/**
	 * Finalizes the withdrawal of the application.
	 * Sets the withdrawn flag to true and updates status to WITHDRAWN.
	 * Called by Career Center Staff after approval or automatically upon other placement acceptance.
	 */
	public void markWithDrawn() {
		this.withdrawn = true;
		this.status = ApplicationStatus.WITHDRAWN;
	}

	/**
	 * Checks if this application is considered "active".
	 * An active application is one that is either PENDING or SUCCESSFUL and has not been withdrawn.
	 *
	 * @return true if active.
	 */
	public boolean isActive() {
		return !this.withdrawn && (this.status == ApplicationStatus.PENDING || this.status == ApplicationStatus.SUCCESSFUL);
	}

	/**
	 * Automatically withdraws this application because the student accepted another placement.
	 * Delegates to {@link #markWithDrawn()}.
	 */
	public void withdrawDueToOtherAcceptance() {
		// simply mark withdrawn and set status
		markWithDrawn();
	}

	/**
	 * Returns a string representation of the Application for debugging purposes.
	 *
	 * @return A string containing application details.
	 */
	@Override
    public String toString() {
        return String.format("Application[ID=%s, Student=%s, Internship=%s, Status=%s, Date=%s, WithdrawalRequested=%b, Withdrawn=%b]",
            applicationID, student.getUserId(), internship.getTitle(), status, 
            applicationDate, withdrawalRequested, withdrawn);
    }
}