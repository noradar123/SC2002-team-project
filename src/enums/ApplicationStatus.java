package enums;

/**
 * Represents the various states of a student's application for an internship.
 */
public enum ApplicationStatus {
    /**
     * The application has been submitted and is awaiting review by the company.
     */
    PENDING,

    /**
     * The application was accepted by the company.
     */
    SUCCESSFUL,

    /**
     * The application was rejected by the company.
     */
    UNSUCCESSFUL,

    /**
     * The student withdrew the application before a final decision was reached.
     */
    WITHDRAWN
}