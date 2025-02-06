package minorProject.studentmgt;

import dao.StudentDAO;
import dao.AttendanceDAO;
import dao.ReportDAO;
import daoimpl.StudentDAOImpl;
import daoimpl.AttendanceDAOImpl;
import daoimpl.ReportDAOImpl;
import model.Student;
import model.Attendance;
import model.Report;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Initialize DAOs
        StudentDAO studentDAO = new StudentDAOImpl();
        AttendanceDAO attendanceDAO = new AttendanceDAOImpl();
        ReportDAO reportDAO = new ReportDAOImpl();
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("--- Student Attendance Management System ---");

            while (true) {
                System.out.println("\n--- Student Attendance Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Mark Attendance");
                System.out.println("4. View Attendance");
                System.out.println("5. Generate Report");
                System.out.println("6. View Report");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        // Add Student
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Course: ");
                        String course = sc.nextLine();
                        Student student = new Student(0, name, course);  // Assuming student ID is auto-generated
                        studentDAO.addStudent(student);
                        break;
                    case 2:
                        // View Students
                        List<Student> students = studentDAO.getStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                        break;

                    case 3:
                        // Mark Attendance
                        System.out.print("Enter Student ID: ");
                        int studentId = sc.nextInt();
                        sc.nextLine();  // Consume newline
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String date = sc.nextLine();
                        System.out.print("Enter Status (Present/Absent/Late): ");
                        String status = sc.nextLine();
                        Attendance attendance = new Attendance(0, studentId, Date.valueOf(date), status);
                        boolean isMarked = attendanceDAO.markAttendance(attendance);
                        if (isMarked) {
                            System.out.println("Attendance marked successfully.");
                        } else {
                            System.out.println("Failed to mark attendance.");
                        }
                        break;

                    case 4:
                        // View Attendance Records
                        List<Attendance> attendanceRecords = attendanceDAO.getAttendanceRecords();
                        if (attendanceRecords.isEmpty()) {
                            System.out.println("No attendance records found.");
                        } else {
                            attendanceRecords.forEach(System.out::println);
                        }
                        break;

                    case 5:
                        // Generate attendance report for a specific student
                        System.out.println("Enter student ID:");
                        int studentId1 = sc.nextInt();
                        System.out.println("Enter month (1-12):");
                        int month = sc.nextInt();
                        System.out.println("Enter year:");
                        int year = sc.nextInt();

                        List<Report> studentReport = reportDAO.generateAttendanceReport(studentId1, month, year);

                        if (studentReport.isEmpty()) {
                            System.out.println("No report found for the given student, month, and year.");
                        } else {
                            System.out.println("\nAttendance Report for Student ID: " + studentId1);
                            for (Report report : studentReport) {
                                // Display report details
                                System.out.println("Student ID: " + report.getStudentId());
                                System.out.println("Month: " + report.getMonth());
                                System.out.println("Year: " + report.getYear());
                                System.out.println("Present Days: " + report.getPresentDays());
                                System.out.println("Absent Days: " + report.getAbsentDays());
                                System.out.println("Late Days: " + report.getLateDays());
                                System.out.println("Total Days: " + report.getTotalDays());
                                System.out.println("Attendance Percentage: " + report.getAttendancePercentage() + "%");
                                System.out.println("=============================================");
                            }
                        }
                        break;

                    case 6:
                        // View Report
                        System.out.print("Enter Student ID: ");
                        int reportStudentId = sc.nextInt();
                        System.out.print("Enter Month (1-12): ");
                        int reportMonth = sc.nextInt();
                        System.out.print("Enter Year: ");
                        int reportYear = sc.nextInt();

                        // Fetch the attendance report
                        List<Report> reportList = reportDAO.generateAttendanceReport(reportStudentId, reportMonth, reportYear);
                        if (reportList.isEmpty()) {
                            System.out.println("No attendance records found for the given student and period.");
                        } else {
                            // Display the report details
                            for (Report r : reportList) {
                                System.out.println("Student ID: " + r.getStudentId());
                                System.out.println("Month: " + r.getMonth());
                                System.out.println("Year: " + r.getYear());
                                System.out.println("Present Days: " + r.getPresentDays());
                                System.out.println("Absent Days: " + r.getAbsentDays());
                                System.out.println("Late Days: " + r.getLateDays());
                                System.out.println("Total Days: " + r.getTotalDays());
                                System.out.println("Attendance Percentage: " + r.getAttendancePercentage() + "%");
                                System.out.println("=============================================");
                            }
                        }
                        break;

                    case 7:
                        // Exit
                        System.out.println("Exiting...");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
