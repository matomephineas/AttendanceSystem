package com.example.faceattendancesystem.DataHelper;

public class TimeTable
{
    public String courseID,courseName,courseCode,subjID,subjName,
            subjCode,timeTableID,startTime,endTime,subjDay,subjVanue;

    public TimeTable() {
    }

    public TimeTable(String courseID, String courseName, String courseCode, String subjID, String subjName, String subjCode, String timeTableID, String startTime, String endTime, String subjDay, String subjVanue) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.subjID = subjID;
        this.subjName = subjName;
        this.subjCode = subjCode;
        this.timeTableID = timeTableID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjDay = subjDay;
        this.subjVanue = subjVanue;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSubjID() {
        return subjID;
    }

    public String getSubjName() {
        return subjName;
    }

    public String getSubjCode() {
        return subjCode;
    }

    public String getTimeTableID() {
        return timeTableID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSubjDay() {
        return subjDay;
    }

    public String getSubjVanue() {
        return subjVanue;
    }
}
