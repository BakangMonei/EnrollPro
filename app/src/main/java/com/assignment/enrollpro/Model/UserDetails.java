package com.assignment.enrollpro.Model;

public class UserDetails {
    private String location;
    private String room;
    private String time;
    private String date;

    public UserDetails(String location, String room, String time, String date) {
        this.location = location;
        this.room = room;
        this.time = time;
        this.date = date;
    }

    // Getters and setters

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}