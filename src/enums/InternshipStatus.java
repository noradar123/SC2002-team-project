package enums;

/**
 * Represents the lifecycle status of an internship listing in the system.
 */
public enum InternshipStatus {
    /** Created by the company but waiting for admin approval. */
    PENDING,
    /** Approved by admin and open for student applications. */
    APPROVED,
    /** Rejected by the admin. */
    REJECTED,
    /** All slots have been taken; no longer accepting applications. */
    FILLED
}