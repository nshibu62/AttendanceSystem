package com.example.attendance_system;

public class AttendanceRecord {
    private String utdId;
    private String studentStatus;
    private String response1;
    private String response2;
    private String response3;
    private String firstName;
    private String middleName;
    private String lastName;
    private String classId;

    public AttendanceRecord(String utdId, String firstName, String middleName, String lastName,String studentStatus,  String response1, String response2, String response3, String classId) {
        this.utdId = utdId;
        this.studentStatus = studentStatus;
        this.response1 = response1;
        this.response2 = response2;
        this.response3 = response3;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.classId = classId;
    }

    // Getter and setter methods for each property
    public String getUtdId() {
        return utdId;
    }

    public void setUtdId(String utdId) {
        this.utdId = utdId;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getResponse1() {
        return response1;
    }

    public void setResponse1(String response1) {
        this.response1 = response1;
    }

    public String getResponse2() {
        return response2;
    }

    public void setResponse2(String response2) {
        this.response2 = response2;
    }

    public String getResponse3() {
        return response3;
    }

    public void setResponse3(String response3) {
        this.response3 = response3;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}

