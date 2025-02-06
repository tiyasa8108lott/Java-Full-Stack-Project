package daoimpl;

import dao.ReportDAO;
import model.Report;
import utility.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    // Method to generate the attendance report for a specific student by student ID, month, and year
  

	@Override
	public List<Report> generateAttendanceReport(int studentId, int month, int year) {
	    String query = "WITH RECURSIVE days_in_month AS ( " +
	                   "    SELECT CAST(CONCAT(?, '-', LPAD(?, 2, '0'), '-01') AS DATE) AS attendance_date " +
	                   "    UNION ALL " +
	                   "    SELECT DATE_ADD(attendance_date, INTERVAL 1 DAY) " +
	                   "    FROM days_in_month " +
	                   "    WHERE MONTH(DATE_ADD(attendance_date, INTERVAL 1 DAY)) = ? " +
	                   ") " +
	                   "SELECT " +
	                   "    s.student_id, " +
	                   "    SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_days, " +
	                   "    SUM(CASE WHEN a.status = 'Absent' THEN 1 ELSE 0 END) AS absent_days, " +
	                   "    SUM(CASE WHEN a.status = 'Late' THEN 1 ELSE 0 END) AS late_days, " +
	                   "    COUNT(dm.attendance_date) AS total_days " +
	                   "FROM students s " +
	                   "LEFT JOIN days_in_month dm ON TRUE " +
	                   "LEFT JOIN attendance a " +
	                   "    ON s.student_id = a.student_id " +
	                   "    AND dm.attendance_date = a.attendance_date " +
	                   "WHERE s.student_id = ? " +
	                   "GROUP BY s.student_id";

	    List<Report> reportList = new ArrayList<>();

	    try (Connection con = ConnectionProvider.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(query)) {

	        pstmt.setInt(1, year);  // Year for the start of the month
	        pstmt.setInt(2, month); // Month for the start of the month
	        pstmt.setInt(3, month); // Month to continue the recursive series
	        pstmt.setInt(4, studentId); // Student ID for filtering

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int presentDays = rs.getInt("present_days");
	            int absentDays = rs.getInt("absent_days");
	            int lateDays = rs.getInt("late_days");
	            int totalDays = rs.getInt("total_days");

	            // Calculate attendance percentage
	            double attendancePercentage = totalDays > 0 ? (presentDays * 100.0 / totalDays) : 0;

	            // Create a Report object
	            Report report = new Report(
	                0, // Report ID (auto-generated)
	                studentId,
	                month,
	                year,
	                presentDays,
	                absentDays,
	                lateDays,
	                totalDays,
	                attendancePercentage
	            );

	            reportList.add(report); // Add the report to the list
	        }
	    } catch (SQLException e) {
	        System.err.println("Error while generating attendance report: " + e.getMessage());
	    }

	    return reportList;
	}


    // Insert the generated report into the database
    private void insertReport(Report report) {
        String query = "INSERT INTO report (student_id, month, year, present_days, absent_days, late_days, total_days, attendance_percentage) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, report.getStudentId());
            pstmt.setInt(2, report.getMonth());
            pstmt.setInt(3, report.getYear());
            pstmt.setInt(4, report.getPresentDays());
            pstmt.setInt(5, report.getAbsentDays());
            pstmt.setInt(6, report.getLateDays());
            pstmt.setInt(7, report.getTotalDays());
            pstmt.setDouble(8, report.getAttendancePercentage());

            pstmt.executeUpdate();  // Insert the report data into the database
        } catch (SQLException e) {
            System.err.println("Error while inserting report: " + e.getMessage());
        }
    }

    // Method to display all the attendance reports
    @Override
    public List<Report> displayReports() {
        String query = "SELECT * FROM report";  // SQL query to fetch all reports
        List<Report> reportList = new ArrayList<>();

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create Report object for each row in the result set
                Report report = new Report(
                        rs.getInt("report_id"),
                        rs.getInt("student_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getInt("present_days"),
                        rs.getInt("absent_days"),
                        rs.getInt("late_days"),
                        rs.getInt("total_days"),
                        rs.getDouble("attendance_percentage")
                );
                reportList.add(report);  // Add to the list
            }
        } catch (SQLException e) {
            System.err.println("Error while displaying attendance reports: " + e.getMessage());
        }

        return reportList;
    }

    // Method to generate attendance reports for all students in a given month and year
    @Override
    public boolean generateReportsForAllStudents(int month, int year) {
        String query = "SELECT s.student_id, " +
                       "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_days, " +
                       "SUM(CASE WHEN a.status = 'Absent' THEN 1 ELSE 0 END) AS absent_days, " +
                       "SUM(CASE WHEN a.status = 'Late' THEN 1 ELSE 0 END) AS late_days, " +
                       "COUNT(a.attendance_date) AS total_days " +
                       "FROM students s " +
                       "LEFT JOIN attendance a ON s.student_id = a.student_id " +
                       "WHERE MONTH(a.attendance_date) = ? AND YEAR(a.attendance_date) = ? " +
                       "GROUP BY s.student_id";

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int studentId = rs.getInt("student_id");

                int presentDays = rs.getInt("present_days");
                int absentDays = rs.getInt("absent_days");
                int lateDays = rs.getInt("late_days");
                int totalDays = rs.getInt("total_days");

                double attendancePercentage = 0;
                if (totalDays > 0) {
                    attendancePercentage = (double) presentDays / totalDays * 100;
                }

                Report report = new Report(
                        0, // Report ID is auto-generated in DB
                        studentId,
                        month,
                        year,
                        presentDays,
                        absentDays,
                        lateDays,
                        totalDays,
                        attendancePercentage
                );

                insertReport(report);  // Insert report into the database
            }
        } catch (SQLException e) {
            System.err.println("Error while generating reports for all students: " + e.getMessage());
            return false;
        }

        return true;
    }

	@Override
	public boolean generateReport(Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Report> getReportsByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Report> getAllReports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Report> getReports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateReport(Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteReport(int reportId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Report> getReportByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return null;
	}
}
