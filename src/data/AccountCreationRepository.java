package data;

import entity.*;
import java.util.*;
import java.util.stream.Collectors;

public class AccountCreationRepository {
    private List<User> users;

    public AccountCreationRepository() {
        this.users = new ArrayList<>();
    }

    // CREATE
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

    // READ
    public User findById(String userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<CompanyRep> findPendingCompanyReps() {
        return users.stream()
                .filter(u -> u instanceof CompanyRep)
                .map(u -> (CompanyRep) u)
                .filter(rep -> !rep.isAuthorized())  
                .collect(Collectors.toList());
    }

    public List<Student> findAllStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public List<CareerCenterStaff> findAllStaff() {
        return users.stream()
                .filter(u -> u instanceof CareerCenterStaff)
                .map(u -> (CareerCenterStaff) u)
                .collect(Collectors.toList());
    }

    // UPDATE
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

    // DELETE
    public boolean delete(String userId) {
        User user = findById(userId);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }

    public boolean existsById(String userId) {
        return findById(userId) != null;
    }
}