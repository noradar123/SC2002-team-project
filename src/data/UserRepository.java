package data;

import entity.User;

public interface UserRepository {
    User findById(String userId);
    void save(User user);
    boolean existsById(String userId);
}
