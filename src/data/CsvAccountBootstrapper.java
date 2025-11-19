package data;

import entity.Student;
import entity.CareerCenterStaff;
import entity.CompanyRep;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvAccountBootstrapper {

    /**
     * 一次把三種使用者從 CSV 載入到既有的 AccountCreationRepository 裡。
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
    private static void loadStudents(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            // 跳過 header: StudentID,Name,Major,Year,Email
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                String id    = p[0].trim();
                String name  = p[1].trim();
                String major = p[2].trim();
                int year     = Integer.parseInt(p[3].trim());
                // String email = p[4].trim(); // 目前沒用到

                // 這裡沒有密碼欄位 → 給一個預設密碼
                String password = id; // 或 "password"，看你喜歡

                Student s = new Student(id, name, password, year, major);
                repo.save(s);
            }

        } catch (IOException e) {
            System.out.println("[CSV ERROR] loadStudents: " + e.getMessage());
        }
    }

    // ===================== Staff =====================
    private static void loadStaff(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            // header: StaffID,Name,Role,Department,Email
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                String id   = p[0].trim();
                String name = p[1].trim();
                String role = p[2].trim();
                String dept = p[3].trim();
                // String email = p[4].trim();

                String password = id; // 預設密碼
                CareerCenterStaff staff = new CareerCenterStaff(id, name, password, dept);
                repo.save(staff);
            }
        } catch (IOException e) {
            System.out.println("[CSV ERROR] loadStaff: " + e.getMessage());
        }
    }

    // ===================== Company Representatives =====================
    private static void loadCompanyReps(AccountCreationRepository repo, String path) {
        if (path == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            br.readLine(); // header

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 7) continue;

                String id       = p[0].trim();
                String name     = p[1].trim();
                String company  = p[2].trim();
                String dept     = p[3].trim();
                String position = p[4].trim();
                String password = id; // 預設密碼，可自行調整

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
