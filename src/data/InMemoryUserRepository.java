package data;

import java.util.HashMap;
import java.util.Map;
import entity.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> store = new HashMap<>();

    public InMemoryUserRepository() {
        // Seed users for testing — password = "password"
        store.put("U2345123F", new Student("U2345123F", "Alice Tan", "password", 3, "CSC"));
        store.put("rep@company.com", new CompanyRep("rep@company.com", "Bob Rep", "password", "Acme", "Quant", "Mgr"));
        store.put("ntu.staff", new CareerCenterStaff("ntu.staff", "Carol Staff", "password", "Careers"));
    }

    @Override
    public User findById(String userId) {
        return store.get(userId);
    }

    @Override
    public void save(User user) {
        store.put(user.getUserId(), user);
    }

    @Override
    public boolean existsById(String userId) {
        return store.containsKey(userId);
    }
}
