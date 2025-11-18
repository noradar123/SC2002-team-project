package data;

import entity.User;

public class AccountCreationUserRepositoryAdapter implements UserRepository {

    private final AccountCreationRepository accountRepo;

    public AccountCreationUserRepositoryAdapter(AccountCreationRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public User findById(String userId) {
        return accountRepo.findById(userId);
    }

    @Override
    public void save(User user) {
        // 若已存在 → update
        if (accountRepo.existsById(user.getUserId())) {
            accountRepo.update(user);
        } else {
            accountRepo.save(user);
        }
    }

    @Override
    public boolean existsById(String userId) {
        return accountRepo.existsById(userId);
    }
}
