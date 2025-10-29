package models;

import config.config;
import java.util.*;

public class StudentModel {

    public static void manageStudents(config db, Scanner sc) {
        while (true) {
            System.out.println("\n--- MANAGE STUDENTS ---");
            System.out.println("[1] Add Student");
            System.out.println("[2] View Students");
            System.out.println("[3] Update Student");
            System.out.println("[4] Delete Student");
            System.out.println("[0] Back");
            System.out.print("Choice: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

            switch (choice) {
                case 1: addStudent(db, sc); break;
                case 2: viewStudents(db); break;
                case 3: updateStudent(db, sc); break;
                case 4: deleteStudent(db, sc); break;
                case 0: return;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    public static void addStudent(config db, Scanner sc) {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter gender: ");
        String gender = sc.nextLine().trim();
        System.out.print("Enter course: ");
        String course = sc.nextLine().trim();

        String sql = "INSERT INTO students (s_name, s_age, s_gender, s_course) VALUES (?, ?, ?, ?)";
        db.addRecord(sql, name, age, gender, course);
    }

    public static void viewStudents(config db) {
        String sql = "SELECT s_id, s_name, s_age, s_gender, s_course FROM students";
        String[] headers = {"ID", "Name", "Age", "Gender", "Course"};
        String[] cols = {"s_id", "s_name", "s_age", "s_gender", "s_course"};
        db.viewRecords(sql, headers, cols);
    }

    public static void updateStudent(config db, Scanner sc) {
        System.out.print("Enter Student ID to update: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New name: ");
        String name = sc.nextLine().trim();
        System.out.print("New age: ");
        int age = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New gender: ");
        String gender = sc.nextLine().trim();
        System.out.print("New course: ");
        String course = sc.nextLine().trim();

        String sql = "UPDATE students SET s_name = ?, s_age = ?, s_gender = ?, s_course = ? WHERE s_id = ?";
        db.updateRecord(sql, name, age, gender, course, id);
    }

    public static void deleteStudent(config db, Scanner sc) {
        System.out.print("Enter Student ID to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        String sql = "DELETE FROM students WHERE s_id = ?";
        db.deleteRecord(sql, id);
    }
}
