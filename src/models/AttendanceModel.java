package models;

import config.config;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceModel {

    // Admin: add attendance record (current date)
    public static void addAttendance(config db, Scanner sc) {
        // show students
        List<Map<String, Object>> students = db.fetchRecords("SELECT s_id, s_name FROM students");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("Available Students:");
        for (Map<String, Object> s : students) {
            System.out.println(s.get("s_id") + " - " + s.get("s_name"));
        }
        System.out.print("Enter Student ID: ");
        int sid = Integer.parseInt(sc.nextLine().trim());

        // show subjects
        List<Map<String, Object>> subjects = db.fetchRecords("SELECT sub_id, sub_name FROM subjects");
        if (subjects.isEmpty()) {
            System.out.println("No subjects found.");
            return;
        }
        System.out.println("Available Subjects:");
        for (Map<String, Object> sub : subjects) {
            System.out.println(sub.get("sub_id") + " - " + sub.get("sub_name"));
        }
        System.out.print("Enter Subject ID: ");
        int subid = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Enter Status (Present/Absent/Late/Excused): ");
        String status = sc.nextLine().trim();

        // current date in ISO format
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String sql = "INSERT INTO attendance (s_id, sub_id, attendance_date, status) VALUES (?, ?, ?, ?)";
        db.addRecord(sql, sid, subid, date, status);
    }

    // Manage attendance menu
    public static void manageAttendance(config db, Scanner sc) {
        while (true) {
            System.out.println("\n--- MANAGE ATTENDANCE ---");
            System.out.println("[1] Add Attendance");
            System.out.println("[2] View Attendance");
            System.out.println("[3] Update Attendance");
            System.out.println("[4] Delete Attendance");
            System.out.println("[0] Back");
            System.out.print("Choice: ");
            int choice = -1;
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

            switch (choice) {
                case 1: addAttendance(db, sc); break;
                case 2: viewAllAttendance(db); break;
                case 3: updateAttendance(db, sc); break;
                case 4: deleteAttendance(db, sc); break;
                case 0: return;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    // Admin / general view of all attendance
    public static void viewAllAttendance(config db) {
        String sql = "SELECT a.a_id, s.s_name, sub.sub_name, a.attendance_date, a.status " +
                     "FROM attendance a " +
                     "JOIN students s ON a.s_id = s.s_id " +
                     "JOIN subjects sub ON a.sub_id = sub.sub_id " +
                     "ORDER BY a.attendance_date DESC";
        String[] headers = {"ID", "Student", "Subject", "Date", "Status"};
        String[] cols = {"a_id", "s_name", "sub_name", "attendance_date", "status"};
        db.viewRecords(sql, headers, cols);
    }

    // Student: view attendance (lookup by username -> student name mapping)
    public static void viewStudentAttendance(config db, String username) {
        // Attempt to find student by matching username to student name, or you can map usernames to s_id
        // First try: user -> users.u_name then match students.s_name
        String findNameSql = "SELECT u_name FROM users WHERE u_username = ?";
        List<Map<String, Object>> urows = db.fetchRecords(findNameSql, username);
        if (urows.isEmpty()) {
            System.out.println("Student record not linked to username.");
            return;
        }
        String u_name = urows.get(0).get("u_name").toString();

        String sql = "SELECT a.a_id, s.s_name, sub.sub_name, a.attendance_date, a.status " +
                     "FROM attendance a " +
                     "JOIN students s ON a.s_id = s.s_id " +
                     "JOIN subjects sub ON a.sub_id = sub.sub_id " +
                     "WHERE s.s_name = ? " +
                     "ORDER BY a.attendance_date DESC";
        String[] headers = {"ID", "Student", "Subject", "Date", "Status"};
        String[] cols = {"a_id", "s_name", "sub_name", "attendance_date", "status"};
        db.viewRecords(sql, headers, cols);
    }

    public static void updateAttendance(config db, Scanner sc) {
        System.out.print("Enter Attendance ID to update: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New status (Present/Absent/Late/Excused): ");
        String status = sc.nextLine().trim();
        
                
        String sql = "UPDATE attendance SET status = ? WHERE a_id = ?";
        db.updateRecord(sql, status, id);
    }

    public static void deleteAttendance(config db, Scanner sc) {
        System.out.print("Enter Attendance ID to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        String sql = "DELETE FROM attendance WHERE a_id = ?";
        db.deleteRecord(sql, id);
    }
}
