package data;

import entity.User;

/**
 * Interface defining the read-only operations for accessing user data.
 * Implementations of this interface are responsible for retrieving user entities
 * without necessarily providing modification capabilities (segregation of duties).
 */
public interface UserLookupRepository {

    /**
     * Finds a user by their unique identifier.
     *
     * @param userId The unique ID of the user (e.g., email).
     * @return The {@link User} object if found; {@code null} otherwise.
     */
    User findById(String userId);

    /**
     * Checks if a user exists in the repository.
     *
     * @param userId The unique ID to check.
     * @return {@code true} if the user exists; {@code false} otherwise.
     */
    boolean existsById(String userId);
}