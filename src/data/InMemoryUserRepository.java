package data;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import entity.Student;
import entity.CompanyRep;
import entity.CareerCenterStaff;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> storage = new HashMap<>();

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

    @Override
    public User findById(String userId) {
        return storage.get(userId);
    }

    @Override
    public void save(User user) {
        storage.put(user.getUserId(), user);
    }

    @Override
    public boolean existsById(String userId) {
        return storage.containsKey(userId);
    }
}
