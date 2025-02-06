package daoimpl;

import dao.StudentDAO;
import model.Student;
import utility.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void addStudent(Student student) {
        String query = "INSERT INTO Students(student_id, name, course) VALUES(?, ?, ?)";
        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, student.getStudentId());  // Using student_id as the column name
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getCourse());

            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.err.println("Error while adding student: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getStudents() {
        String query = "SELECT * FROM Students";
        List<Student> studentList = new ArrayList<>();

        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Use column names that match the table's column names (e.g., 'student_id' instead of 'studentId')
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),  // Ensure the column name matches the one in the database
                        rs.getString("name"),
                        rs.getString("course")
                );
                studentList.add(student);
            }

            if (studentList.isEmpty()) {
                System.out.println("No students found in the database.");
            }
            return studentList;

        } catch (SQLException e) {
            System.err.println("Error while fetching students: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void updateStudent(Student student) {
        String query = "UPDATE Students SET name = ?, course = ? WHERE student_id = ?";
        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getCourse());
            pstmt.setInt(3, student.getStudentId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("No student found with ID: " + student.getStudentId());
            }
        } catch (SQLException e) {
            System.err.println("Error while updating student: " + e.getMessage());
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        String query = "DELETE FROM Students WHERE student_id = ?";
        try (Connection con = ConnectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, studentId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.err.println("Error while deleting student: " + e.getMessage());
        }
    }
}
