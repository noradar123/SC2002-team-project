package control;

import data.UserLookupRepository;
import data.UserWriteRepository;
import entity.CompanyRep;
import entity.User;

/**
 * Service class responsible for user authentication and credential management.
 * Handles login verification against the repository, including specific checks for 
 * Company Representative authorization status, and handles password updates.
 */
public class AuthService {

    private final UserLookupRepository lookup;
    private final UserWriteRepository write;

    /**
     * Constructs a new AuthService.
     *
     * @param lookup The repository used to find users by ID.
     * @param write  The repository used to persist user data (e.g., updated passwords).
     */
    public AuthService(UserLookupRepository lookup, UserWriteRepository write) {
        this.lookup = lookup;
        this.write = write;
    }

    /**
     * Authenticates a user based on their ID and password.
     * Performs validation checks for:
     * <ul>
     * <li>Empty credentials.</li>
     * <li>Invalid User ID.</li>
     * <li>Incorrect Password.</li>
     * <li>Company Representative authorization status (must be approved by staff).</li>
     * </ul>
     *
     * @param userId      The unique identifier of the user.
     * @param rawPassword The plain-text password input.
     * @return The authenticated {@link User} object.
     * @throws AuthException If any validation check fails.
     */
    public User authenticate(String userId, String rawPassword) throws AuthException {
        if (isBlank(userId) || isBlank(rawPassword)) {
            throw new AuthException("Missing credentials. Please enter both ID and password.");
        }

        User u = lookup.findById(userId);
        if (u == null) {
            throw new AuthException("Invalid ID. Please check your User ID and try again.");
        }
        if (!u.verifyPassword(rawPassword)) {
            throw new AuthException("Incorrect password. Please try again.");
        }
        if (u instanceof CompanyRep && !((CompanyRep) u).isAuthorized()) {
            throw new AuthException("Your account is pending approval from Career Center Staff.");
        }

        return u;
    }

    /**
     * Updates a user's password.
     * Validates that the old password is correct and that the new password is different from the old one.
     *
     * @param userId The unique identifier of the user.
     * @param oldPwd The current password (for verification).
     * @param newPwd The new password to set.
     * @throws AuthException If fields are empty, user not found, old password incorrect, or new password matches the old one.
     */
    public void changePassword(String userId, String oldPwd, String newPwd) throws AuthException {
        if (isBlank(userId) || isBlank(oldPwd) || isBlank(newPwd)) {
            throw new AuthException("Please provide current and new passwords.");
        }

        User u = lookup.findById(userId);
        if (u == null) throw new AuthException("User not found.");
        if (!u.verifyPassword(oldPwd)) throw new AuthException("Current password is incorrect.");
        if (newPwd.equals(oldPwd)) throw new AuthException("New password cannot be the same as current password.");

        u.setPassword(newPwd);
        write.save(u);    // <-- uses write repository
    }

    // Private helper to check for empty strings
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    /**
     * Custom exception thrown when an authentication or authorization check fails.
     */
    public static class AuthException extends Exception {
        /**
         * Constructs a new AuthException with a specific error message.
         * @param msg The detailed error message.
         */
        public AuthException(String msg) { super(msg); }
    }
}