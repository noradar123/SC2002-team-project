package control;
import entity.*;
import data.AccountCreationRepository;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AccountCreationController {
    private AccountCreationRepository repository;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public AccountCreationController(AccountCreationRepository repository) {
        this.repository = repository;
    }
    
    // ========== COMPANY REP REGISTRATION ==========
    public CompanyRep registerCompanyRep(String email, String name, String password,
                                         String companyName, String department, String position) {
        //Validate email format
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
        return (CompanyRep) repository.save(rep);
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
        
        rep.setAuthorized(true);
        
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
    public List<CompanyRep> getPendingCompanyReps() {
    	List<User> allUsers = repository.getAllUsers();
    	return allUsers.stream()
                .filter(user -> user instanceof CompanyRep)
                .map(user -> (CompanyRep) user)
                .filter(rep -> !rep.isAuthorized())
                .collect(Collectors.toList());
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