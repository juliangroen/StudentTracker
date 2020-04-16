package com.jgroen.juliangroenstudenttracker.features.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText textCourseNote;
    private EditText textInstructorName;
    private EditText textInstructorNumber;
    private EditText textInstructorEmail;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_edit);

        textCourseTitle = findViewById(R.id.textCourseTitle);
        textCourseStatus = findViewById(R.id.textCourseStatus);
        dateStartDate = findViewById(R.id.dateCourseStartDate);
        dateEndDate = findViewById(R.id.dateCourseEndDate);
        textCourseNote = findViewById(R.id.editCourseNote);
        textInstructorName = findViewById(R.id.editCourseInstructorName);
        textInstructorNumber = findViewById(R.id.editCourseInstructorNumber);
        textInstructorEmail = findViewById(R.id.editCourseInstructorEmail);

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
            textCourseNote.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_NOTE));
            textInstructorName.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NAME));
            textInstructorNumber.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NUMBER));
            textInstructorEmail.setText(intent.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_EMAIL));
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

        String note = textCourseNote.getText().toString();
        String name = textInstructorName.getText().toString();
        String number = textInstructorNumber.getText().toString();
        String email = textInstructorEmail.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(CourseDetailsActivity.EXTRA_COURSE_ID)) {

            CourseEntity course = new CourseEntity(intent.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID,-1), title, startDate, endDate, status);
            course.setCourseID(intent.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_ID, -1));
            course.setCourseNote(note);
            course.setCourseInstructorName(name);
            course.setCourseInstructorNumber(number);
            course.setCourseInstructorEmail(email);
            courseViewModel.update(course);

        }

        Intent data = new Intent();
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, intent.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, -1));
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE, title);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS, status);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, startDate.getTime());
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, endDate.getTime());
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_NOTE, note);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NAME, name);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NUMBER, number);
        data.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_EMAIL, email);
        setResult(RESULT_OK, data);
        finish();

    }
}
