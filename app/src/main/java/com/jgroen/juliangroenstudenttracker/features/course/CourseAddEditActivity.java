package com.jgroen.juliangroenstudenttracker.features.course;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.term.TermActivity;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;
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

        if (intent.hasExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT)) {
            setTitle(R.string.activity_course_edit_title);

            CourseEntity course = (CourseEntity) intent.getSerializableExtra(
                    CourseDetailsActivity.EXTRA_COURSE_OBJECT);

            textCourseTitle.setText(course.getCourseTitle());
            textCourseStatus.setText(course.getCourseStatus());

            int[] startDateArray = TrackerUtilities.longToDateArray(course.getCourseStartDate().getTime());

            int[] endDateArray = TrackerUtilities.longToDateArray(course.getCourseEndDate().getTime());

            dateStartDate.updateDate(startDateArray[0], startDateArray[1], startDateArray[2]);
            dateEndDate.updateDate(endDateArray[0], endDateArray[1], endDateArray[2]);
            textCourseNote.setText(course.getCourseNote());
            textInstructorName.setText(course.getCourseInstructorName());
            textInstructorNumber.setText(course.getCourseInstructorNumber());
            textInstructorEmail.setText(course.getCourseInstructorEmail());

        }

        FloatingActionButton fab = findViewById(R.id.fabCourseSave);
        fab.setOnClickListener(view -> {
            saveCourse();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
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
            if (status.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a title and status", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (status.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a status", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT)) {

            CourseEntity course = (CourseEntity) intent.getSerializableExtra(
                    CourseDetailsActivity.EXTRA_COURSE_OBJECT);
            course.setCourseTitle(title);
            course.setCourseStartDate(startDate);
            course.setCourseEndDate(endDate);
            course.setCourseStatus(status);
            course.setCourseNote(note);
            course.setCourseInstructorName(name);
            course.setCourseInstructorNumber(number);
            course.setCourseInstructorEmail(email);
            courseViewModel.update(course);
            Intent data = new Intent();
            data.putExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT, course);
            setResult(RESULT_OK, data);
            finish();

        } else {

            TermEntity term = (TermEntity) intent.getSerializableExtra(TermActivity.EXTRA_TERM_OBJ);
            Intent data = new Intent();
            data.putExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, term.getTermID());
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
}
