package boundary;

import control.AccountCreationController;
import java.util.Scanner;

/**
 * An abstract view class responsible for handling the user interface logic for account creation.
 * This class implements the <b>Template Method Design Pattern</b>. It defines the skeleton of the 
 * account creation workflow in {@link #createAccount()} while deferring specific steps 
 * (like gathering role-specific data) to its subclasses.
 */
public abstract class AccountCreationView {
    
    /**
     * The controller used to process account creation logic.
     */
    protected AccountCreationController controller;
    
    /**
     * The scanner instance for reading user input.
     */
    protected Scanner scanner;
    
    /**
     * Constructs a new AccountCreationView.
     * Initializes the scanner and links the view to the provided controller.
     *
     * @param controller The controller responsible for handling account creation logic.
     */
    public AccountCreationView(AccountCreationController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * The <b>Template Method</b> that executes the account creation workflow.
     * <p>
     * The sequence of steps is:
     * <ol>
     * <li>Get User ID (Abstract)</li>
     * <li>Get Name (Shared)</li>
     * <li>Get Password (Shared)</li>
     * <li>Collect Additional Data (Abstract Hook)</li>
     * <li>Create Account in System (Abstract)</li>
     * <li>Display Success/Error Message</li>
     * </ol>
     * </p>
     */
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
    
    /**
     * Prompts the user to enter their User ID.
     * Subclasses should implement this to provide specific prompt text (e.g., "Enter Email" vs "Enter Matric Number").
     *
     * @return The user ID string entered by the user.
     */
    protected abstract String getUserId();

    /**
     * A hook method for subclasses to collect role-specific data.
     * (e.g., Company Name for Reps, or Major for Students).
     */
    protected abstract void collectAdditionalData();

    /**
     * Delegates the final account creation call to the controller.
     * Subclasses implement this to call the specific registration method on the controller (e.g., registerStudent vs registerCompanyRep).
     *
     * @param userId   The User ID.
     * @param name     The User Name.
     * @param password The User Password.
     */
    protected abstract void createAccountInSystem(String userId, String name, String password);

    /**
     * Displays a success message specific to the type of account created.
     */
    protected abstract void displaySuccessMessage();
    
    /**
     * Prompts the user to enter their full name.
     *
     * @return The name entered by the user.
     */
    protected String getName() {
        System.out.print("Enter name: ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Prompts the user to enter their password.
     *
     * @return The password entered by the user.
     */
    protected String getPassword() {
        System.out.print("Enter password: ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Displays an error message to the console.
     *
     * @param message The error message to display.
     */
    protected void displayErrorMessage(String message) {
        System.out.println("❌ Error: " + message);
    }
}