package entity;

import filter.CompositeFilter;

/**
 * An abstract base class representing a generic user in the system.
 * This class encapsulates common attributes (ID, name, password) and behaviors (authentication, filtering)
 * shared by all user types (Students, Company Reps, and Staff).
 */
public abstract class User {
    
    private final String userId;
    private final String name;
    private String password;
    private CompositeFilter<Internship> filter;
    
    /**
     * Constructs a new User.
     *
     * @param userId   The unique identifier for the user (typically an email or ID string).
     * @param name     The full name of the user.
     * @param password The login password.
     * @throws IllegalArgumentException If userId, name, or password is null.
     */
    public User(String userId, String name, String password) {
        if (userId == null || name == null || password == null) {
            throw new IllegalArgumentException("userId, name, and password must not be null");
        }
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.filter = null;
    }

    /**
     * Retrieves the unique user ID.
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the user's full name.
     * @return The name.
     */
    public String getName() {
        return name;
    }
 
    /**
     * Updates the user's password.
     *
     * @param newPwd The new password string.
     * @throws IllegalArgumentException If the new password is null.
     */
    public void setPassword(String newPwd) {
        if (newPwd == null) throw new IllegalArgumentException("newPwd must not be null");
        this.password = newPwd;
    }
    
    /**
     * Verifies if the provided password matches the stored password.
     *
     * @param raw The password input to check.
     * @return {@code true} if the passwords match; {@code false} otherwise.
     */
    public boolean verifyPassword(String raw) {
        return raw != null && raw.equals(this.password); // swap to PasswordHasher later if you add one
    }
    
    /**
     * Retrieves the raw password.
     * Note: In a production environment, avoid exposing raw passwords.
     * @return The password string.
     */
    public String getPassword() {
    	return password;
    }
    
    /**
     * Retrieves the composite filter associated with this user.
     * This filter determines which internships the user can see or prefers to see.
     * @return The {@link CompositeFilter} object.
     */
    public CompositeFilter<Internship> getFilter() {
		return filter;
	}

    /**
     * Sets the composite filter for this user.
     * @param filter The filter configuration to apply.
     */
    public void setFilter(CompositeFilter<Internship> filter) {
        this.filter = filter;
    }
}