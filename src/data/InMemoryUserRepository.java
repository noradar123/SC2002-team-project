package data;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import entity.Student;
import entity.CompanyRep;
import entity.CareerCenterStaff;

/**
 * An in-memory implementation of the user repository interfaces.
 * This class uses a {@link HashMap} to store user data temporarily during runtime.
 * It is primarily useful for testing or development purposes where persistent storage is not required.
 */
public class InMemoryUserRepository implements UserLookupRepository, UserWriteRepository {
    
    private final Map<String, User> storage = new HashMap<>();

    /**
     * Constructs a new InMemoryUserRepository.
     * <p>
     * <b>Note:</b> This constructor pre-loads the repository with hardcoded dummy data
     * (one Staff, one Company Rep, and one Student) to facilitate immediate testing.
     * </p>
     */
    public InMemoryUserRepository() {
        storage.put("staff1",
                new CareerCenterStaff(
                        "staff1",
                        "NTU Career Staff",
                        "password",
                        "Career Centre"
                )
        );
        storage.put("rep1",
                new CompanyRep(
                        "rep1",
                        "Alice Rep",
                        "password",
                        "Acme Corp",
                        "Tech",
                        "HR Manager"
                )
        );
        storage.put("U2345123F",
                new Student(
                        "U2345123F",
                        "Bob Student",
                        "password",
                        3,
                        "CSC"
                )
        );
    }

    /**
     * Retrieves a user from the in-memory storage by their ID.
     *
     * @param userId The unique identifier of the user.
     * @return The {@link User} object if found; {@code null} otherwise.
     */
    @Override
    public User findById(String userId) {
        return storage.get(userId);
    }

    /**
     * Saves or updates a user in the in-memory storage.
     *
     * @param user The user entity to store.
     */
    @Override
    public void save(User user) {
        storage.put(user.getUserId(), user);
    }

    /**
     * Checks if a user exists in the in-memory storage.
     *
     * @param userId The unique identifier to check.
     * @return {@code true} if the user exists; {@code false} otherwise.
     */
    @Override
    public boolean existsById(String userId) {
        return storage.containsKey(userId);
    }
}