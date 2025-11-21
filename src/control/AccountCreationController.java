package control;

import entity.*;
import data.AccountCreationRepository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing the lifecycle of user account creation.
 * This includes registering new Company Representatives, handling the approval workflow
 * (approve/reject) for those representatives, and general user authentication.
 */

public class AccountCreationController {
    private final AccountCreationRepository repository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    /**
     * Constructs a new AccountCreationController.
     *
     * @param repository The data repository used for accessing and persisting user account data.
     */
    public AccountCreationController(AccountCreationRepository repository) {
        this.repository = repository;
    }

    // ========== COMPANY REP REGISTRATION ==========
    /**
     * Registers a new Company Representative account.
     * The account is created with a status requiring approval before they can log in.
     *
     * @param email       The email address of the representative (must be unique and valid format).
     * @param name        The full name of the representative.
     * @param password    The login password.
     * @param companyName The name of the company the representative belongs to.
     * @param department  The department within the company.
     * @param position    The job title/position of the representative.
     * @return The newly created {@link CompanyRep} object.
     * @throws IllegalArgumentException If the email format is invalid, email exists, or company name is missing.
     */
    public CompanyRep registerCompanyRep(String email, String name, String password,
                                         String companyName, String department, String position) {

        // Validate email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check if user already exists
        if (repository.existsById(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Validate required fields
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required");
        }

        // Create company rep (NOT active - requires approval)
        CompanyRep rep = new CompanyRep(
                email, name, password, companyName, department, position
        );
        repository.save(rep);
        return rep;
    }

    // ========== APPROVAL WORKFLOW ==========
    /**
     * Approves a pending Company Representative, authorizing them to access the system.
     *
     * @param repId The unique identifier (email) of the representative to approve.
     * @return The updated {@link CompanyRep} object with authorized status set to true.
     * @throws IllegalArgumentException If the user is not found or is not a Company Representative.
     */
    public CompanyRep approveCompanyRep(String repId) {
        User user = repository.findById(repId);

        if (user == null) {
            throw new IllegalArgumentException("Company representative not found: " + repId);
        }

        if (!(user instanceof CompanyRep)) {
            throw new IllegalArgumentException("User is not a company representative");
        }

        CompanyRep rep = (CompanyRep) user;
        rep.setAuthorized(true);
        repository.update(rep);
        return rep;
    }
    /**
     * Rejects a pending Company Representative application and deletes the record.
     *
     * @param repId The unique identifier (email) of the representative to reject.
     * @throws IllegalArgumentException If the user is not found or is not a Company Representative.
     */
    public void rejectCompanyRep(String repId) {
        User user = repository.findById(repId);

        if (user == null) {
            throw new IllegalArgumentException("Company representative not found: " + repId);
        }

        if (!(user instanceof CompanyRep)) {
            throw new IllegalArgumentException("User is not a company representative");
        }

        repository.delete(repId);
    }

    // ========== QUERY METHODS ==========
    /**
     * Retrieves a list of all Company Representatives who are currently pending approval.
     *
     * @return A list of pending {@link CompanyRep} objects.
     */
    public List<CompanyRep> getPendingCompanyReps() {
        return repository.findPendingCompanyReps();
    }
    /**
     * Retrieves all users currently registered in the system.
     *
     * @return A list of all {@link User} objects.
     */
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }
    /**
     * Retrieves a specific user by their unique identifier.
     *
     * @param userId The unique identifier (email) of the user.
     * @return The {@link User} object if found.
     * @throws IllegalArgumentException If the user does not exist.
     */
    public User getUserById(String userId) {
        User user = repository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }
    /**
     * Verifies user credentials for login.
     *
     * @param userId   The user's identifier (email).
     * @param password The password to verify.
     * @return {@code true} if the user exists and the password matches; {@code false} otherwise.
     */
    public boolean canUserLogin(String userId, String password) {
        User user = repository.findById(userId);

        if (user == null) {
            return false;
        }

        if (!user.getPassword().equals(password)) {
            return false;
        }

        return true;
    }
}
