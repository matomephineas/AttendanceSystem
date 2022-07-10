package com.example.faceattendancesystem.DataHelper;

public class User {
    public String name,email,phone,password,userID,studentNumber,image;
    public int userType;
    public User(){}

    public User(String name, String email, String phone, String password, String userID, String studentNumber, String image, int userType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userID = userID;
        this.studentNumber = studentNumber;
        this.image = image;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
