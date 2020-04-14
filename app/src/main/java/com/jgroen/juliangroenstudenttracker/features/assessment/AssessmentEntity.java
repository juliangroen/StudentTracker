package com.jgroen.juliangroenstudenttracker.features.assessment;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = CourseEntity.class,
                parentColumns = "courseID",
                childColumns = "courseID",
                onDelete = CASCADE))
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private int courseID;
    private String assessmentTitle;
    private String assessmentType;
    private Date assessmentDueDate;

    public AssessmentEntity(int courseID, String assessmentTitle, String assessmentType, Date assessmentDueDate) {
        this.courseID = courseID;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentDueDate = assessmentDueDate;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Date getAssessmentDueDate() {
        return assessmentDueDate;
    }

    public void setAssessmentDueDate(Date assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }
}
