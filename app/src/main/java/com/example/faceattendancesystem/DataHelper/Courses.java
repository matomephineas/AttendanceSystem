package com.example.faceattendancesystem.DataHelper;

public class Courses
{
    public String courseID,courseName,courseCode,courseFaculty;
    public Courses(){}

    public Courses(String courseID, String courseName, String courseCode, String courseFaculty) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseFaculty = courseFaculty;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseFaculty() {
        return courseFaculty;
    }

    public void setCourseFaculty(String courseFaculty) {
        this.courseFaculty = courseFaculty;
    }
}
