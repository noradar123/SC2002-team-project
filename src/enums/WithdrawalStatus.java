package enums;

/**
 * Represents the status of a request to withdraw from an internship.
 */
public enum WithdrawalStatus {
    /** The withdrawal request is awaiting admin review. */
    PENDING,
    /** The withdrawal has been approved. */
    APPROVED,
    /** The withdrawal has been rejected. */
    REJECTED
}