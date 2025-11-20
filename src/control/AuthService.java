package control;

import data.UserLookupRepository;
import data.UserWriteRepository;
import entity.User;

public class AuthService {

    private final UserLookupRepository lookup;
    private final UserWriteRepository write;

    public AuthService(UserLookupRepository lookup, UserWriteRepository write) {
        this.lookup = lookup;
        this.write = write;
    }

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

        return u;
    }

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

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    public static class AuthException extends Exception {
        public AuthException(String msg) { super(msg); }
    }
}
