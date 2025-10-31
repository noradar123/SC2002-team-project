import java.time.LocalDate;

public class WithdrawalRequest {
    // Attributes
    private String requestId;
    private String studentId;
    private String internshipId;
    private String reason;
    private LocalDate requestDate;
    private WithdrawalStatus status;

    // Constructor
    public WithdrawalRequest(String requestId, String studentId, String internshipId) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.requestDate = LocalDate.now();
        this.status = WithdrawalStatus.PENDING;
    }

    // Methods
    public void submitRequest(String reason) {
        this.reason = reason;
        this.requestDate = LocalDate.now();
        this.status = WithdrawalStatus.PENDING;
        System.out.println("Withdrawal request submitted for student " + studentId + " with reason: " + reason);
    }

    public void updateStatus(WithdrawalStatus status) {
        this.status = status;
        System.out.println("Withdrawal request " + requestId + " updated to status: " + status);
    }

    // Getters
    public String getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }
}
