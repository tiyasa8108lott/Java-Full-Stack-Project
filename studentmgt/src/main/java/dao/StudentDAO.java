package dao;
import java.util.List;

import model.Student;


public interface StudentDAO {
	
	void addStudent(Student student);     // Add a new student
    List<Student> getStudents();          // Retrieve all students
    void updateStudent(Student student);  // Update a student's details
    void deleteStudent(int studentId);    // Delete a student by ID

}
