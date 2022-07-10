package com.example.faceattendancesystem.DataHelper;

public class AttendanceReport {
    String subjectName,studentNo,studentEmail,attendanceStatus,attendanceDate,attendanceTime,attendanceID;

    public AttendanceReport() {
    }

    public AttendanceReport(String subjectName, String studentNo, String studentEmail, String attendanceStatus, String attendanceDate, String attendanceTime, String attendanceID) {
        this.subjectName = subjectName;
        this.studentNo = studentNo;
        this.studentEmail = studentEmail;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.attendanceTime = attendanceTime;
        this.attendanceID = attendanceID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(String attendanceID) {
        this.attendanceID = attendanceID;
    }
}
