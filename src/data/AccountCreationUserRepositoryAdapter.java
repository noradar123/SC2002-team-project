package data;

import entity.User;

/**
 * An adapter class that bridges the concrete {@link AccountCreationRepository} 
 * to the generic {@link UserLookupRepository} and {@link UserWriteRepository} interfaces.
 * This allows services like AuthService to interact with user data without being tightly coupled 
 * to the specific implementation of account creation storage.
 */
public class AccountCreationUserRepositoryAdapter implements UserLookupRepository, UserWriteRepository {

    private final AccountCreationRepository accountRepo;

    /**
     * Constructs a new AccountCreationUserRepositoryAdapter.
     *
     * @param accountRepo The concrete repository instance to delegate operations to.
     */
    public AccountCreationUserRepositoryAdapter(AccountCreationRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Finds a user by their unique identifier.
     * Delegates to {@link AccountCreationRepository#findById(String)}.
     *
     * @param userId The ID of the user to find.
     * @return The User object if found, null otherwise.
     */
    @Override
    public User findById(String userId) {
        return accountRepo.findById(userId);
    }

    /**
     * Saves or updates a user in the repository.
     * Checks if the user exists; if so, calls update, otherwise calls save.
     *
     * @param user The user entity to persist.
     */
    @Override
    public void save(User user) {
        if (accountRepo.existsById(user.getUserId())) {
            accountRepo.update(user);
        } else {
            accountRepo.save(user);
        }
    }

    /**
     * Checks if a user exists by their unique identifier.
     * Delegates to {@link AccountCreationRepository#existsById(String)}.
     *
     * @param userId The ID to check.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean existsById(String userId) {
        return accountRepo.existsById(userId);
    }
}