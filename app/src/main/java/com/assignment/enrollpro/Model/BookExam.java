package com.assignment.enrollpro.Model;

/**
 * @Author: AMG
 * @Date: Wednesday 21/02/2024 February 2024
 * @Time: 01:57
 */

public class BookExam {
    private String moduleLeaderEmail, moduleLeader, studentEmail, studentIDNumber, firstName, lastName, phoneNumber, examRoom, Faculty, moduleName, dateTime;

    public BookExam(String moduleLeaderEmail, String moduleLeader, String studentEmail, String studentIDNumber, String firstName, String lastName, String phoneNumber, String examRoom, String Faculty, String moduleName, String dateTime) {
        this.moduleLeaderEmail = moduleLeaderEmail;
        this.moduleLeader = moduleLeader;
        this.studentEmail = studentEmail;
        this.studentIDNumber = studentIDNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.examRoom = examRoom;
        this.Faculty = Faculty;
        this.moduleName = moduleName;
        this.dateTime = dateTime;
    }

    public BookExam(){
    }

    public String getModuleLeaderEmail() {
        return moduleLeaderEmail;
    }

    public void setModuleLeaderEmail(String moduleLeaderEmail) {
        this.moduleLeaderEmail = moduleLeaderEmail;
    }

    public String getModuleLeader() {
        return moduleLeader;
    }

    public void setModuleLeader(String moduleLeader) {
        this.moduleLeader = moduleLeader;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentIDNumber() {
        return studentIDNumber;
    }

    public void setStudentIDNumber(String studentIDNumber) {
        this.studentIDNumber = studentIDNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(String examRoom) {
        this.examRoom = examRoom;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
