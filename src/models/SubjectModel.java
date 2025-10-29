package models;

import config.config;
import java.util.*;

public class SubjectModel {

    public static void manageSubjects(config db, Scanner sc) {
        while (true) {
            System.out.println("\n--- MANAGE SUBJECTS ---");
            System.out.println("[1] Add Subject");
            System.out.println("[2] View Subjects");
            System.out.println("[3] Update Subject");
            System.out.println("[4] Delete Subject");
            System.out.println("[0] Back");
            System.out.print("Choice: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

            switch (choice) {
                case 1: addSubject(db, sc); break;
                case 2: viewSubjects(db); break;
                case 3: updateSubject(db, sc); break;
                case 4: deleteSubject(db, sc); break;
                case 0: return;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    public static void addSubject(config db, Scanner sc) {
        System.out.print("Enter subject code: ");
        String code = sc.nextLine().trim();
        System.out.print("Enter subject name: ");
        String name = sc.nextLine().trim();
        String sql = "INSERT INTO subjects (sub_code, sub_name) VALUES (?, ?)";
        db.addRecord(sql, code, name);
    }

    public static void viewSubjects(config db) {
        String sql = "SELECT sub_id, sub_code, sub_name FROM subjects";
        String[] headers = {"ID", "Code", "Name"};
        String[] cols = {"sub_id", "sub_code", "sub_name"};
        db.viewRecords(sql, headers, cols);
    }

    public static void updateSubject(config db, Scanner sc) {
        System.out.print("Enter Subject ID to update: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New code: ");
        String code = sc.nextLine().trim();
        System.out.print("New name: ");
        String name = sc.nextLine().trim();
        String sql = "UPDATE subjects SET sub_code = ?, sub_name = ? WHERE sub_id = ?";
        db.updateRecord(sql, code, name, id);
    }

    public static void deleteSubject(config db, Scanner sc) {
        System.out.print("Enter Subject ID to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        String sql = "DELETE FROM subjects WHERE sub_id = ?";
        db.deleteRecord(sql, id);
    }
}
