package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor


public class Student {
	
	private int studentId;   // Unique ID for the student
    private String name;     // Name of the student
    private String course;   // Course name or details

}
