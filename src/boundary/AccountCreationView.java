package boundary;

import control.AccountCreationController;
import java.util.Scanner;


public abstract class AccountCreationView {
	protected AccountCreationController controller;
    protected Scanner scanner;
    
    public AccountCreationView(AccountCreationController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }
    
    // Template method
    public void createAccount() {
        System.out.println("\n=== Account Creation ===");
        
        try {
            String userId = getUserId();
            String name = getName();
            String password = getPassword();
            
            collectAdditionalData();
            createAccountInSystem(userId, name, password);
            displaySuccessMessage();
            
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    protected abstract String getUserId();
    protected abstract void collectAdditionalData();
    protected abstract void createAccountInSystem(String userId, String name, String password);
    protected abstract void displaySuccessMessage();
    
    protected String getName() {
        System.out.print("Enter name: ");
        return scanner.nextLine().trim();
    }
    
    protected String getPassword() {
        System.out.print("Enter password: ");
        return scanner.nextLine().trim();
    }
    
    protected void displayErrorMessage(String message) {
        System.out.println("❌ Error: " + message);
    }
}

