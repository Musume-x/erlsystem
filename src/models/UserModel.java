package models;

import config.config;
import java.util.*;

public class UserModel {

    // Register a new user (Admin or Student)
    public static void register(config db, Scanner sc) {
        System.out.print("Enter full name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter username (unique): ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();
        System.out.print("Enter role (Admin/Student): ");
        String role = sc.nextLine().trim();

        String hashed = db.hashPassword(pass);
        String sql = "INSERT INTO users (u_name, u_username, u_pass, u_role, u_status) VALUES (?, ?, ?, ?, ?)";
        db.addRecord(sql, name, username, hashed, role, "Active");

        System.out.println("User registered successfully!");
    }
}
