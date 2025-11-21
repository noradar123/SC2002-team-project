package data;

import entity.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Repository class responsible for handling the data access for user account creation and management.
 * This class provides methods to Create, Read, Update, and Delete (CRUD) user entities 
 * (Students, Company Representatives, and Staff) from the underlying storage.
 */
public class AccountCreationRepository {
    private List<User> users;
    /**
     * Constructs a new AccountCreationRepository.
     * Initializes connection to the data storage (e.g., CSV files or memory).
     */
    public AccountCreationRepository() {
        this.users = new ArrayList<>();
    }

    /**
     * Saves a new user to the repository.
     *
     * @param user The user object to be saved.
     * @return The saved user object.
     */
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (findById(user.getUserId()) != null) {
            throw new IllegalArgumentException("User ID already exists: " + user.getUserId());
        }

        users.add(user);
        return user;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId The unique identifier of the user.
     * @return The {@link User} object if found; {@code null} otherwise.
     */
    public User findById(String userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    /**
     * Retrieves all users currently existing in the system.
     *
     * @return A list of all {@link User} objects.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    /**
     * Retrieves a list of Company Representatives who are pending approval.
     * These are users who have registered but have not yet been authorized by Staff.
     *
     * @return A list of pending {@link CompanyRep} objects.
     */
    public List<CompanyRep> findPendingCompanyReps() {
        return users.stream()
                .filter(u -> u instanceof CompanyRep)
                .map(u -> (CompanyRep) u)
                .filter(rep -> !rep.isAuthorized())  
                .collect(Collectors.toList());
    }
    /**
     * Retrieves a list of all registered Students.
     *
     * @return A list of {@link Student} objects.
     */
    public List<Student> findAllStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }
    /**
     * Retrieves a list of all Career Center Staff members.
     *
     * @return A list of {@link CareerCenterStaff} objects.
     */
    public List<CareerCenterStaff> findAllStaff() {
        return users.stream()
                .filter(u -> u instanceof CareerCenterStaff)
                .map(u -> (CareerCenterStaff) u)
                .collect(Collectors.toList());
    }

    /**
     * Updates the details of an existing user in the repository.
     *
     * @param user The user object containing updated information.
     * @return The updated user object.
     */
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        User existing = findById(user.getUserId());
        if (existing == null) {
            throw new IllegalArgumentException("User not found: " + user.getUserId());
        }

        int index = users.indexOf(existing);
        users.set(index, user);
        return user;
    }

    /**
     * Deletes a user from the repository.
     *
     * @param userId The unique identifier of the user to delete.
     * @return {@code true} if the user was found and deleted; {@code false} otherwise.
     */
    public boolean delete(String userId) {
        User user = findById(userId);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    /**
     * Checks if a user exists in the repository with the given ID.
     *
     * @param userId The unique identifier to check.
     * @return {@code true} if the user exists; {@code false} otherwise.
     */
    public boolean existsById(String userId) {
        return findById(userId) != null;
    }
}