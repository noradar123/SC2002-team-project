package boundary;

import control.AccountCreationController;
import entity.CompanyRep;

public class CompanyRepAccountCreationView extends AccountCreationView {
	private String companyName;
    private String department;
    private String position;
    
    public CompanyRepAccountCreationView(AccountCreationController controller) {
        super(controller);
    }
    
    @Override
    protected String getUserId() {
        System.out.print("Enter Company Email: ");
        return scanner.nextLine().trim();
    }
    
    @Override
    protected void collectAdditionalData() {
        System.out.print("Enter Company Name: ");
        this.companyName = scanner.nextLine().trim();
        
        System.out.print("Enter Department: ");
        this.department = scanner.nextLine().trim();
        
        System.out.print("Enter Position: ");
        this.position = scanner.nextLine().trim();
    }
    
    @Override
    protected void createAccountInSystem(String userId, String name, String password) {
        CompanyRep rep = controller.registerCompanyRep(
            userId, name, password, companyName, department, position
        );
        System.out.println("Company representative account created with email: " + rep.getUserId());
    }
    
    @Override
    protected void displaySuccessMessage() {
        System.out.println("✅ Company representative account created successfully!");
        System.out.println("⏳ Your account is pending approval from Career Center Staff.");
        System.out.println("You will be able to log in once approved.");
    }
}

