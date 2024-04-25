package com.assignment.enrollpro.Model;
/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */

public class Search {
    private String name;
    private String email;
    private String phone;
    private String address;

    public Search(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Search{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String toCSV() {
        return name + "," + email + "," + phone + "," + address;
    }

    public static String getCSVHeader() {
        return "Name,Email,Phone,Address";
    }

    public static Search fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Search(parts[0], parts[1], parts[2], parts[3]);
    }

    public static Search fromString(String s) {
        String[] parts = s.split(",");
        return new Search(parts[0], parts[1], parts[2], parts[3]);
    }

    public String toFileString() {
        return name + "\n" + email + "\n" + phone + "\n" + address;
    }

    public static Search fromFileString(String s) {
        String[] parts = s.split("\n");
        return new Search(parts[0], parts[1], parts[2], parts[3]);
    }

    public static String getFileHeader() {
        return "Name\nEmail\nPhone\nAddress";
    }

    public static String getFileName() {
        return "search.txt";
    }

    public static String getCSVFileName() {
        return "search.csv";
    }

    public static String getJSONFileName() {
        return "search.json";
    }

    public static String getXMLFileName() {
        return "search.xml";
    }

    public static String getHTMLFileName() {
        return "search.html";
    }
}
