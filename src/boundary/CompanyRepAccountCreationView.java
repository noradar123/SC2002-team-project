package boundary;

import control.AccountCreationController;
import entity.CompanyRep;

/**
 * A concrete view class responsible for the user interface logic when creating a Company Representative account.
 * This class extends {@link AccountCreationView} and implements the specific steps to collect
 * company-related details (Name, Department, Position).
 */
public class CompanyRepAccountCreationView extends AccountCreationView {
    
    private String companyName;
    private String department;
    private String position;
    
    /**
     * Constructs a new CompanyRepAccountCreationView.
     *
     * @param controller The controller responsible for handling account creation logic.
     */
    public CompanyRepAccountCreationView(AccountCreationController controller) {
        super(controller);
    }
    
    /**
     * Prompts the user to enter their Company Email as the User ID.
     *
     * @return The email string entered by the user.
     */
    @Override
    protected String getUserId() {
        System.out.print("Enter Company Email: ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Collects specific data required for a Company Representative account.
     * Prompts for Company Name, Department, and Position.
     */
    @Override
    protected void collectAdditionalData() {
        System.out.print("Enter Company Name: ");
        this.companyName = scanner.nextLine().trim();
        
        System.out.print("Enter Department: ");
        this.department = scanner.nextLine().trim();
        
        System.out.print("Enter Position: ");
        this.position = scanner.nextLine().trim();
    }
    
    /**
     * Calls the controller to register a new Company Representative using the collected data.
     *
     * @param userId   The user ID (Email).
     * @param name     The user's name.
     * @param password The user's password.
     */
    @Override
    protected void createAccountInSystem(String userId, String name, String password) {
        CompanyRep rep = controller.registerCompanyRep(
            userId, name, password, companyName, department, position
        );
        System.out.println("Company representative account created with email: " + rep.getUserId());
    }
    
    /**
     * Displays a success message specific to Company Representatives.
     * explicitly warns the user that their account requires staff approval before login is possible.
     */
    @Override
    protected void displaySuccessMessage() {
        System.out.println("✅ Company representative account created successfully!");
        System.out.println("⏳ Your account is pending approval from Career Center Staff.");
        System.out.println("You will be able to log in once approved.");
    }
}