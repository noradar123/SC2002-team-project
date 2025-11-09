package TestPlayground;

import entity.*;
import control.*;
import data.*;

public class TESTCASE5 {
    public static void main(String[] args) {
    	System.out.println("===Test case 5: Company Representative Account Creation.==="
    			+ "\nRequirement: Company Rep can only login AFTER authorization.");
        
        AccountCreationRepository repository = new AccountCreationRepository();
        AccountCreationController controller = new AccountCreationController(repository);

        try {
            // PART 1: Company Representative Registration
        	System.out.println("--Step 1 : Company Representative Registration--");
            
            CompanyRep newRep = controller.registerCompanyRep(
                "john.doe@techcorp.com",
                "John Doe",
                "password",
                "TechCorp Solutions",
                "Human Resources",
                "Recruitment Manager"
            );
            
            System.out.println("Company Rep registered successfully");
            System.out.println("  - Email: " + newRep.getUserId());
            System.out.println("  - Name: " + newRep.getName());
            System.out.println("  - Company: " + newRep.getCompany());
            System.out.println("  - Account Status: " + (newRep.isAuthorized() ? "AUTHORIZED" : "PENDING AUTHORIZATION"));

            // PART 2: Attempt Login BEFORE Authorization (Should FAIL)
            System.out.println("--Step 2 : Attempt Login BEFORE Authorization--");
            
            boolean canLoginBefore = controller.canUserLogin("john.doe@techcorp.com", "password");
            
            if (!canLoginBefore) {
                System.out.println("Login CORRECTLY DENIED (account not authorized yet)");
                System.out.println("Expected: false");
                System.out.println("Actual: " + canLoginBefore);
            } else {
                System.out.println("BUG DETECTED: User can't login without authorization!");
                //return; //break the whole program if the user is not authorized. 
            }

            // PART 3: View Pending Authorizations (Career Center Staff)
            System.out.println("--Step 3 : Career Center Staff Views Pending Requests--");
            
            var pendingReps = controller.getPendingCompanyReps();
            System.out.println("Pending company representatives: " + pendingReps.size());
            
            for (CompanyRep rep : pendingReps) {
                System.out.println("  - " + rep.getName() + " (" + rep.getUserId() + ")");
                System.out.println("    Company: " + rep.getCompany());
                System.out.println("    Position: " + rep.getPosition());
                System.out.println("    Status: PENDING AUTHORIZATION");
            }


            // PART 4: Career Center Staff Authorizes Account
            System.out.println("--Step 4: Career Center Staff Authorizes Account--");
            
            CompanyRep authorizedRep = controller.approveCompanyRep("john.doe@techcorp.com");
            
            System.out.println(" Account authorized successfully");
            System.out.println(" Email: " + authorizedRep.getUserId());
            System.out.println(" Account Status: " + (authorizedRep.isAuthorized() ? "AUTHORIZED" : "PENDING"));

            // PART 5: Attempt Login AFTER Authorization (Should SUCCEED)
            System.out.println("--Step 5 : Attempt Login AFTER Authorization--");
            
            boolean canLoginAfter = controller.canUserLogin("john.doe@techcorp.com", "password");
            
            if (canLoginAfter) {
                System.out.println("Login SUCCESSFULLY GRANTED (account authorized)");
            } else {
                System.out.println("BUG DETECTED: User cannot login after authorization!");
                return;
            }
            
            System.out.println("Summary of test cases: ");
            System.out.println("Company Rep registration creates UNAUTHORIZED account"+
            "\nCompany Rep CANNOT login before authorization" +
            "\nCareer Center Staff can view pending authorizations"+
            "\nCareer Center Staff can authorize accounts" + 
            "\nCompany Rep CAN login after authorization");

        } catch (Exception e) {
            System.out.println("\n TEST FAILED WITH ERROR:");
            System.out.println("   " + e.getMessage());
            e.printStackTrace();
        }
    }
}

