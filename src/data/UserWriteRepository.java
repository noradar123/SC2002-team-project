package data;

import entity.User;

/**
 * Interface defining the write operations for user data persistence.
 * Implementations of this interface are responsible for saving or updating user entities
 * in the underlying storage mechanism (e.g., database, CSV, memory).
 */
public interface UserWriteRepository {

    /**
     * Persists a user entity to the repository.
     * If the user already exists, this method typically updates the existing record.
     * If the user is new, it creates a new record.
     *
     * @param user The user entity to save.
     */
    void save(User user);
}