package com.jgroen.juliangroenstudenttracker.features.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.util.Date;
import java.util.GregorianCalendar;

public class CourseAddEditActivity extends AppCompatActivity {

    private EditText textCourseTitle;
    private EditText textCourseStatus;
    private DatePicker dateStartDate;
    private DatePicker dateEndDate;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_edit);

        textCourseTitle = findViewById(R.id.textCourseTitle);
        textCourseStatus = findViewById(R.id.textCourseStatus);
        dateStartDate = findViewById(R.id.dateCourseStartDate);
        dateEndDate = findViewById(R.id.dateCourseEndDate);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        Intent intent = getIntent();

        if (intent.hasExtra(CourseDetailsActivity.EXTRA_COURSE_ID)) {
            setTitle(R.string.activity_course_edit_title);
            textCourseTitle.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE));
            textCourseStatus.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS));

            int[] startDateArray = TrackerUtilities.longToDateArray(
                    intent.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, -1));

            int[] endDateArray = TrackerUtilities.longToDateArray(
                    intent.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, -1));

            dateStartDate.updateDate(startDateArray[0], startDateArray[1], startDateArray[2]);
            dateEndDate.updateDate(endDateArray[0], endDateArray[1], endDateArray[2]);
        }

        FloatingActionButton fab = findViewById(R.id.fabCourseSave);
        fab.setOnClickListener(view -> {
            saveCourse();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    private void saveCourse() {

        String title = textCourseTitle.getText().toString();
        String status = textCourseStatus.getText().toString();

        Date startDate = new GregorianCalendar(
                dateStartDate.getYear(),
                dateStartDate.getMonth(),
                dateStartDate.getDayOfMonth()
        ).getTime();

        Date endDate = new GregorianCalendar(
                dateEndDate.getYear(),
                dateEndDate.getMonth(),
                dateEndDate.getDayOfMonth()
        ).getTime();

        if (title.trim().isEmpty()) {
            Snackbar.make(findViewById(R.id.activityCourseAddEdit), "Please insert a title", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(CourseDetailsActivity.EXTRA_COURSE_ID)) {

            CourseEntity course = new CourseEntity(title, startDate, endDate, status);
            course.setCourseID(intent.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_ID, -1));
            courseViewModel.update(course);

        }

        Intent data = new Intent();
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE, title);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS, status);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, startDate.getTime());
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, endDate.getTime());
        setResult(RESULT_OK, data);
        finish();

    }
}
