package com.assignment.enrollpro.Model;

public class BookExam {
    private String moduleLeaderEmail, moduleLeaderName, studentEmail, studentIDNumber, firstName, lastName,
            phoneNumber, examRoom, faculty, moduleName, dateAndTime, qrCodeValue;

    public BookExam(String moduleLeaderEmail, String moduleLeaderName, String studentEmail, String studentIDNumber, String firstName, String lastName, String phoneNumber, String examRoom, String faculty, String moduleName, String dateAndTime, String qrCodeValue) {
        this.moduleLeaderEmail = moduleLeaderEmail;
        this.moduleLeaderName = moduleLeaderName;
        this.studentEmail = studentEmail;
        this.studentIDNumber = studentIDNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.examRoom = examRoom;
        this.faculty = faculty;
        this.moduleName = moduleName;
        this.dateAndTime = dateAndTime;
        this.qrCodeValue = qrCodeValue;
    }

    public BookExam() {
        super();
    }

    public String getModuleLeaderEmail() {
        return moduleLeaderEmail;
    }

    public void setModuleLeaderEmail(String moduleLeaderEmail) {
        this.moduleLeaderEmail = moduleLeaderEmail;
    }

    public String getModuleLeaderName() {
        return moduleLeaderName;
    }

    public void setModuleLeaderName(String moduleLeaderName) {
        this.moduleLeaderName = moduleLeaderName;
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
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
    public void setQrCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }
    public String getQrCodeValue() {
        return qrCodeValue;
    }
}
