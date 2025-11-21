package entity;

import enums.WithdrawalStatus;
import java.time.LocalDate;

/**
 * Represents a formal request submitted by a Student to withdraw from an applied or accepted internship.
 * This entity tracks the justification for withdrawal and its current administrative approval status.
 */
public class WithdrawalRequest {
    // Attributes
    private String requestId;
    private String studentId;
    private String internshipId;
    private String reason;
    private LocalDate requestDate;
    private WithdrawalStatus status;

    /**
     * Constructs a new WithdrawalRequest.
     * Initializes the request with the current date and a default PENDING status.
     *
     * @param requestId    The unique identifier for this request.
     * @param studentId    The ID of the student making the request.
     * @param internshipId The ID of the internship being withdrawn from.
     */
    public WithdrawalRequest(String requestId, String studentId, String internshipId) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.requestDate = LocalDate.now();
        this.status = WithdrawalStatus.PENDING;
    }

    // Methods

    /**
     * Submits or updates the withdrawal request with a specific reason.
     * Resets the request date to the current date and ensures status is PENDING.
     *
     * @param reason The textual explanation for why the student wishes to withdraw.
     */
    public void submitRequest(String reason) {
        this.reason = reason;
        this.requestDate = LocalDate.now();
        this.status = WithdrawalStatus.PENDING;
    }

    /**
     * Updates the administrative status of the request.
     * Typically called by Career Center Staff when approving or rejecting the withdrawal.
     *
     * @param status The new {@link WithdrawalStatus}.
     */
    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    // Getters

    /**
     * Retrieves the unique request ID.
     * @return The request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Retrieves the ID of the student who made the request.
     * @return The student ID.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Retrieves the ID of the internship involved in the request.
     * @return The internship ID.
     */
    public String getInternshipId() {
        return internshipId;
    }

    /**
     * Retrieves the reason provided by the student.
     * @return The reason string.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Retrieves the date the request was last submitted or updated.
     * @return The request date.
     */
    public LocalDate getRequestDate() {
        return requestDate;
    }

    /**
     * Retrieves the current status of the withdrawal request.
     * @return The {@link WithdrawalStatus} (e.g., PENDING, APPROVED).
     */
    public WithdrawalStatus getStatus() {
        return status;
    }
}