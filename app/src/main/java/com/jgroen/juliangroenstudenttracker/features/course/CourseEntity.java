package com.jgroen.juliangroenstudenttracker.features.course;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private String courseTitle;
    private Date courseStartDate;
    private Date courseEndDate;
    private String courseStatus;

    public CourseEntity(String courseTitle, Date courseStartDate, Date courseEndDate, String courseStatus) {
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.courseStatus = courseStatus;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public Date getCourseStartDate() {
        return courseStartDate;
    }

    public Date getCourseEndDate() {
        return courseEndDate;
    }

    public String getCourseStatus() {
        return courseStatus;
    }
}
