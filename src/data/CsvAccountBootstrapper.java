package data;

import entity.Student;
import entity.CareerCenterStaff;
import entity.CompanyRep;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class responsible for bootstrapping (seeding) user accounts from CSV files.
 * This class parses external CSV data and populates the {@link AccountCreationRepository}
 * with initial Student, Staff, and Company Representative accounts.
 */
public class CsvAccountBootstrapper {

    /**
     * Constructs a new CsvAccountBootstrapper.
     * (Note: This class is primarily used via its static methods).
     */
    public CsvAccountBootstrapper() {
        // Default constructor
    }

    /**
     * Orchestrates the loading of all user types into the repository.
     *
     * @param repo          The repository where user accounts will be saved.
     * @param studentCsv    The file path to the Student CSV data.
     * @param staffCsv      The file path to the Career Center Staff CSV data.
     * @param companyRepCsv The file path to the Company Representative CSV data.
     */
    public static void loadAllUsers(AccountCreationRepository repo,
                                    String studentCsv,
                                    String staffCsv,
                                    String companyRepCsv) {
        loadStudents(repo, studentCsv);
        loadStaff(repo, staffCsv);
        loadCompanyReps(repo, companyRepCsv);
    }

    // ===================== Students =====================

    /**
     * Parses the Student CSV file and saves entities to the repository.
     * Expected CSV format: ID, Name, Major, Year.
     *
     * @param repo The repository to save to.
     * @param path The CSV file path.
     */
    private static void loadStudents(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                String id    = p[0].trim();
                String name  = p[1].trim();
                String major = p[2].trim();
                int year     = Integer.parseInt(p[3].trim());

                // Default password is set to the User ID
                String password = id;
                Student s = new Student(id, name, password, year, major);
                repo.save(s);
            }

        } catch (IOException e) {
            System.out.println("[CSV ERROR] loadStudents: " + e.getMessage());
        }
    }

    // ===================== Staff =====================

    /**
     * Parses the Staff CSV file and saves entities to the repository.
     * Expected CSV format: ID, Name, Role, Department.
     *
     * @param repo The repository to save to.
     * @param path The CSV file path.
     */
    private static void loadStaff(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                String id   = p[0].trim();
                String name = p[1].trim();
                // String role = p[2].trim(); // Role unused in constructor, kept for CSV alignment
                String dept = p[3].trim();
                // String email = p[4].trim();

                String password = id;
                CareerCenterStaff staff = new CareerCenterStaff(id, name, password, dept);
                repo.save(staff);
            }
        } catch (IOException e) {
            System.out.println("[CSV ERROR] loadStaff: " + e.getMessage());
        }
    }

    // ===================== Company Representatives =====================

    /**
     * Parses the Company Representative CSV file and saves entities to the repository.
     * Expected CSV format: ID, Name, Company, Department, Position.
     *
     * @param repo The repository to save to.
     * @param path The CSV file path.
     */
    private static void loadCompanyReps(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 7) continue;

                String id       = p[0].trim();
                String name     = p[1].trim();
                String company  = p[2].trim();
                String dept     = p[3].trim();
                String position = p[4].trim();
                String password = id;

                CompanyRep rep = new CompanyRep(
                        id,
                        name,
                        password,
                        company,
                        dept,
                        position
                );
                repo.save(rep);
            }
        } catch (IOException e) {
            System.out.println("[CSV ERROR] loadCompanyReps: " + e.getMessage());
        }
    }
}