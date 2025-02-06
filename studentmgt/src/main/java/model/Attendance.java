package model;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor

public class Attendance {
 
	private int attendanceId;  // Unique ID for the attendance record
    private int studentId;     // Student ID (foreign key)
    private Date attendanceDate; // Date of attendance
    private String status;     // Status: Present/Absent/Late
}
