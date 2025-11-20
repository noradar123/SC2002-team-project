package data;

import entity.User;

public interface UserLookupRepository {
    User findById(String userId);
    boolean existsById(String userId);

}
