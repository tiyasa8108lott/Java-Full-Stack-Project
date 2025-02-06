package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Report {

    private int reportId;           // The ID of the report (primary key in DB)
    private int studentId;          // The ID of the student
    private int month;              // The month of the report
    private int year;               // The year of the report
    private int presentDays;        // The number of days the student was present
    private int absentDays;         // The number of days the student was absent
    private int lateDays;           // The number of days the student was late
    private int totalDays;          // The total number of attendance days (sum of present, absent, late)
    private double attendancePercentage;  // The attendance percentage for the student

}


