package dao;
import java.util.List;

import model.Attendance;


public interface AttendanceDAO {
	boolean markAttendance(Attendance attendance); // Add attendance record
    List<Attendance> getAttendanceRecords();   // Retrieve all attendance records
    List<Attendance> getAttendanceByStudentId(int studentId); // Get attendance for a specific student
	boolean updateAttendance(Attendance attendance);
	boolean deleteAttendance(int attendanceId);
	boolean markAttendance(int studentId, String date, String status);

}
