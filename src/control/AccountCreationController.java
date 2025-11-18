package control;

import entity.*;
import data.AccountCreationRepository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AccountCreationController {
    private final AccountCreationRepository repository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public AccountCreationController(AccountCreationRepository repository) {
        this.repository = repository;
    }

    // ========== COMPANY REP REGISTRATION ==========

    public CompanyRep registerCompanyRep(String email, String name, String password,
                                         String companyName, String department, String position) {

        // Validate email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check if user already exists
        if (repository.existsById(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Validate required fields
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required");
        }

        // Create company rep (NOT active - requires approval)
        CompanyRep rep = new CompanyRep(
                email, name, password, companyName, department, position
        );

        // 新增帳號
        repository.save(rep);

        return rep;
    }

    // ========== APPROVAL WORKFLOW ==========

    public CompanyRep approveCompanyRep(String repId) {
        User user = repository.findById(repId);

        if (user == null) {
            throw new IllegalArgumentException("Company representative not found: " + repId);
        }

        if (!(user instanceof CompanyRep)) {
            throw new IllegalArgumentException("User is not a company representative");
        }

        CompanyRep rep = (CompanyRep) user;

        // 設為已授權
        rep.setAuthorized(true);

        // ★ 用 update，而不是 save（因為這個 user 已經在 list 裡了）
        repository.update(rep);

        return rep;
    }

    public void rejectCompanyRep(String repId) {
        User user = repository.findById(repId);

        if (user == null) {
            throw new IllegalArgumentException("Company representative not found: " + repId);
        }

        if (!(user instanceof CompanyRep)) {
            throw new IllegalArgumentException("User is not a company representative");
        }

        repository.delete(repId);
    }

    // ========== QUERY METHODS ==========

    /** 給 CareerCenterStaffController 用，抓所有 pending reps */
    public List<CompanyRep> getPendingCompanyReps() {
        // 你已經在 repository 有 findPendingCompanyReps()，直接用就好
        return repository.findPendingCompanyReps();
    }

    /** 若之後有需要全部 user 的話（目前好像用不到） */
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public User getUserById(String userId) {
        User user = repository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }

    public boolean canUserLogin(String userId, String password) {
        User user = repository.findById(userId);

        if (user == null) {
            return false;
        }

        if (!user.getPassword().equals(password)) {
            return false;
        }

        return true;
    }
}
