package com.jgroen.juliangroenstudenttracker.features.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

public class CourseDetailsActivity extends AppCompatActivity {

    public static final int ADD_COURSE_REQUEST_CODE = 1;
    public static final int EDIT_COURSE_REQUEST_CODE = 2;
    public static final String EXTRA_COURSE_ID = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TERM_ID = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_TERM_ID";
    public static final String EXTRA_COURSE_TITLE = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_STATUS = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_START_DATE = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_END_DATE = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_NOTE = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_NOTE";
    public static final String EXTRA_COURSE_INSTRUCTOR_NAME = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_INSTRUCTOR_NAME";
    public static final String EXTRA_COURSE_INSTRUCTOR_NUMBER = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_INSTRUCTOR_NUMBER";
    public static final String EXTRA_COURSE_INSTRUCTOR_EMAIL = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_INSTRUCTOR_EMAIL";

    private TextView textCourseDetailTitle;
    private TextView textCourseDetailDates;
    private TextView textCourseDetailStatus;
    private TextView textCourseDetailNote;
    private TextView textCourseInstructorName;
    private TextView textCourseInstructorNumber;
    private TextView textCourseInstructorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        textCourseDetailTitle = findViewById(R.id.textCourseDetailTitle);
        textCourseDetailDates = findViewById(R.id.textCourseDetailDates);
        textCourseDetailStatus = findViewById(R.id.textCourseDetailStatus);
        textCourseDetailNote = findViewById(R.id.textCourseDetailNote);
        textCourseInstructorName = findViewById(R.id.textCourseInstructorName);
        textCourseInstructorNumber = findViewById(R.id.textCourseInstructorNumber);
        textCourseInstructorEmail = findViewById(R.id.textCourseInstructorEmail);

        Intent intent = getIntent();

        setData(intent);

        FloatingActionButton fab = findViewById(R.id.fabAssessmentAdd);
        fab.setOnClickListener(view -> {
            addAssessment();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_edit) {

            Intent firstIntent = getIntent();
            Intent intent = new Intent(CourseDetailsActivity.this, CourseAddEditActivity.class);
            intent.putExtras(firstIntent);
            startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);
            return true;

        } else {

            return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_COURSE_REQUEST_CODE) {
                setData(data);
                Snackbar.make(findViewById(R.id.activityCourseDetails), "Term Updated!", Snackbar.LENGTH_SHORT).show();
            } else {
                setData(getIntent());
            }
        }
    }

    private void addAssessment() {
    }

    private void setData(Intent intent) {
        String startDate = TrackerUtilities.longToDateString(intent.getLongExtra(EXTRA_COURSE_START_DATE, -1));
        String endDate = TrackerUtilities.longToDateString(intent.getLongExtra(EXTRA_COURSE_END_DATE, -1));

        textCourseDetailTitle.setText(intent.getStringExtra(EXTRA_COURSE_TITLE));
        textCourseDetailTitle.setTypeface(null, Typeface.BOLD);
        textCourseDetailStatus.setText(intent.getStringExtra(EXTRA_COURSE_STATUS));
        textCourseDetailDates.setText(getString(R.string.detail_dates, startDate, endDate));
        textCourseDetailNote.setText(intent.getStringExtra(EXTRA_COURSE_NOTE));
        textCourseInstructorName.setText(getString(R.string.course_instructor_name,
                (intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_NAME) != null)
                        ? intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_NAME) : ""));
        textCourseInstructorNumber.setText(getString(R.string.course_instructor_Number,
                (intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_NUMBER) != null)
                        ? intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_NUMBER) : ""));
        textCourseInstructorEmail.setText(getString(R.string.course_instructor_Email,
                (intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_EMAIL) != null)
                        ? intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_EMAIL) : ""));
    }
}
