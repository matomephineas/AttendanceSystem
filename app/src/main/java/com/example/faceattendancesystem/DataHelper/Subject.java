package com.example.faceattendancesystem.DataHelper;

public class Subject {
    String courseID,courseCode,subjectID,subjectName,subjectCode,courseFaculty;

    public Subject() {}

    public Subject(String courseID, String courseCode, String subjectID, String subjectName, String subjectCode, String courseFaculty) {
        this.courseID = courseID;
        this.courseCode = courseCode;
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.courseFaculty = courseFaculty;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getCourseFaculty() {
        return courseFaculty;
    }

    public void setCourseFaculty(String courseFaculty) {
        this.courseFaculty = courseFaculty;
    }
}
