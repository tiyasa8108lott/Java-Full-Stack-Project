package dao;
import java.util.List;

import model.Report;

public interface ReportDAO {
	boolean generateReport(Report report);              // Add a new report
    List<Report> getReportsByStudentId(int studentId); // Get reports for a specific student
    List<Report> getAllReports();                     // Retrieve all reports
	List<Report> getReports();
	boolean updateReport(Report report);
	boolean deleteReport(int reportId);
	List<Report> getReportByStudentId(int studentId);
	List<Report> generateAttendanceReport(int studentIdForReport, int month, int year);
	boolean generateReportsForAllStudents(int month, int year);
	List<Report> displayReports();

}
