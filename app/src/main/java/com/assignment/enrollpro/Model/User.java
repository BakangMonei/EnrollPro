package com.assignment.enrollpro.Model;

public class User {

    private String firstname, lastname, email, phoneNumber, username;
    private String profileImageUrl;

    public User() {
    }

    public User(String firstname, String lastname, String email, String phoneNumber, String username, String profileImageUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public void setFullName(String fullName) {
        this.firstname = fullName.split(" ")[0];
        this.lastname = fullName.split(" ")[1];
    }

    public String toCSV() {
        return firstname + "," + lastname + "," + email + "," + phoneNumber + "," + username;
    }



}
