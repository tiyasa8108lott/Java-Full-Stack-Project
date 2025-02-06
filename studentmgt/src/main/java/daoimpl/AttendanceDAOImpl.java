package daoimpl;

import dao.AttendanceDAO;
import model.Attendance;
import utility.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAOImpl implements AttendanceDAO {

    // Mark attendance for a student on a specific date with a status
    public boolean markAttendance(int studentId, String date, String status) {
        String checkStudentQuery = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmtCheck = con.prepareStatement(checkStudentQuery)) {
            
            pstmtCheck.setInt(1, studentId);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Student exists, now mark attendance
                String insertAttendanceQuery = "INSERT INTO attendance (student_id, attendance_date, status) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtInsert = con.prepareStatement(insertAttendanceQuery)) {
                    pstmtInsert.setInt(1, studentId);
                    pstmtInsert.setString(2, date);
                    pstmtInsert.setString(3, status);
                    pstmtInsert.executeUpdate();
                    return true;
                }
            } else {
                System.err.println("Student with ID " + studentId + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all attendance records
    @Override
    public List<Attendance> getAttendanceRecords() {
        String query = "SELECT * FROM attendance";
        List<Attendance> attendanceList = new ArrayList<>();

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Attendance attendance = new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getDate("attendance_date"),
                        rs.getString("status")
                );
                attendanceList.add(attendance);
            }

            if (attendanceList.isEmpty()) {
                System.out.println("No attendance records found.");
            }
            return attendanceList;

        } catch (SQLException e) {
            System.err.println("Error while fetching attendance records: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get attendance by specific student ID
    @Override
    public List<Attendance> getAttendanceByStudentId(int studentId) {
        String query = "SELECT * FROM attendance WHERE student_id = ?";
        List<Attendance> attendanceList = new ArrayList<>();

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getDate("attendance_date"),
                        rs.getString("status")
                );
                attendanceList.add(attendance);
            }

            if (attendanceList.isEmpty()) {
                System.out.println("No attendance records found for Student ID " + studentId);
            }
            return attendanceList;

        } catch (SQLException e) {
            System.err.println("Error while fetching attendance records: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Update attendance record
    @Override
    public boolean updateAttendance(Attendance attendance) {
        String updateQuery = "UPDATE attendance SET attendance_date = ?, status = ? WHERE attendance_id = ?";

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setDate(1, attendance.getAttendanceDate());
            pstmt.setString(2, attendance.getStatus());
            pstmt.setInt(3, attendance.getAttendanceId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error while updating attendance: " + e.getMessage());
            return false;
        }
    }

    // Delete attendance record by attendance ID
    @Override
    public boolean deleteAttendance(int attendanceId) {
        String deleteQuery = "DELETE FROM attendance WHERE attendance_id = ?";

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, attendanceId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error while deleting attendance: " + e.getMessage());
            return false;
        }
    }

    // Additional mark attendance method with Attendance object
    @Override
    public boolean markAttendance(Attendance attendance) {
        String checkStudentQuery = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmtCheck = con.prepareStatement(checkStudentQuery)) {

            pstmtCheck.setInt(1, attendance.getStudentId());
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Student exists, now mark attendance
                String insertAttendanceQuery = "INSERT INTO attendance (student_id, attendance_date, status) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtInsert = con.prepareStatement(insertAttendanceQuery)) {
                    pstmtInsert.setInt(1, attendance.getStudentId());
                    pstmtInsert.setDate(2, attendance.getAttendanceDate());
                    pstmtInsert.setString(3, attendance.getStatus());
                    pstmtInsert.executeUpdate();
                    return true;
                }
            } else {
                System.err.println("Student with ID " + attendance.getStudentId() + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
