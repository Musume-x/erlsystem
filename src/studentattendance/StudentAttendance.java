package StudentAttendance;

import config.config;
import models.*;
import java.util.*;

public class StudentAttendance {
    private static Scanner sc = new Scanner(System.in);
    private static config db = new config();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== ATTENDANCE MANAGEMENT SYSTEM ===");
            System.out.println("[1] Register (student/admin)");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choose: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}
            switch (choice) {
                case 1: UserModel.register(db, sc); break;
                case 2: loginFlow(); break;
                case 3: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    private static void loginFlow() {
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();

        String hashed = db.hashPassword(pass);
        String sql = "SELECT * FROM users WHERE u_username = ? AND u_pass = ? AND u_status = ?";
        List<Map<String, Object>> rows = db.fetchRecords(sql, username, hashed, "Active");

        if (rows.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }

        Map<String, Object> user = rows.get(0);
        String role = user.get("u_role").toString();

        System.out.println("\nLogin successful! Welcome, " + user.get("u_name") + " (" + role + ")");

        if (role.equalsIgnoreCase("Admin")) {
            adminMenu();
        } else {
            studentMenu(user); // pass user map for student actions
        }
    }

    // Admin menu
    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("[1] Manage Students");
            System.out.println("[2] Manage Subjects");
            System.out.println("[3] Manage Attendance");
            System.out.println("[4] View Attendance Records");
            System.out.println("[5] Logout");
            System.out.print("Choose: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

            switch (choice) {
                case 1: StudentModel.manageStudents(db, sc); break;
                case 2: SubjectModel.manageSubjects(db, sc); break;
                case 3: AttendanceModel.manageAttendance(db, sc); break;
                case 4: AttendanceModel.viewAllAttendance(db); break;
                case 5: System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    // Student menu (userMap contains user info, optionally link user -> student)
    private static void studentMenu(Map<String, Object> user) {
        while (true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("[1] View My Attendance");
            System.out.println("[2] Logout");
            System.out.print("Choose: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

            switch (choice) {
                case 1:
                    // If students table and users are linked by username or name, locate student's s_id
                    // We'll assume u_username equals a student's unique identifier (or name) — adjust if different
                    String username = user.get("u_username").toString();
                    AttendanceModel.viewStudentAttendance(db, username);
                    break;
                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
