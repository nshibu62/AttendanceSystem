package com.example.attendance_system;

public class StudentAttendanceRecord {
    private String date;
    private String studentStatus;

    public StudentAttendanceRecord(String date, String studentStatus) {
        this.date = date;
        this.studentStatus = studentStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }
}