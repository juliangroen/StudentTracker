package com.jgroen.juliangroenstudenttracker.features.course;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentAdapter;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentAddEditActivity;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentDetailsActivity;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentEntity;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentViewModel;
import com.jgroen.juliangroenstudenttracker.utils.TrackerReceiver;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity implements AssessmentAdapter.AdapterCallback {

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
    public static final String EXTRA_COURSE_OBJECT = "com.jgroen.juliangroenstudenttracker.EXTRA_COURSE_OBJECT";

    private TextView textCourseDetailTitle;
    private TextView textCourseDetailDates;
    private TextView textCourseDetailStatus;
    private TextView textCourseDetailNote;
    private ImageButton iconCourseDetailNote;
    private TextView textCourseInstructorName;
    private TextView textCourseInstructorNumber;
    private TextView textCourseInstructorEmail;

    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        textCourseDetailTitle = findViewById(R.id.textCourseDetailTitle);
        textCourseDetailDates = findViewById(R.id.textCourseDetailDates);
        textCourseDetailStatus = findViewById(R.id.textCourseDetailStatus);
        textCourseDetailNote = findViewById(R.id.textCourseDetailNote);
        iconCourseDetailNote = findViewById(R.id.iconCourseDetailNote);
        textCourseInstructorName = findViewById(R.id.textCourseInstructorName);
        textCourseInstructorNumber = findViewById(R.id.textCourseInstructorNumber);
        textCourseInstructorEmail = findViewById(R.id.textCourseInstructorEmail);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        Intent intent = getIntent();

        setData(intent);

        FloatingActionButton fab = findViewById(R.id.fabAssessmentAdd);
        fab.setOnClickListener(view -> {
            addAssessment();
        });

        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        final AssessmentAdapter adapter = new AssessmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                CourseEntity course = (CourseEntity) intent.getSerializableExtra(EXTRA_COURSE_OBJECT);
                int courseID = course.getCourseID();
                List<AssessmentEntity> assessmentList = new ArrayList<AssessmentEntity>();
                for (AssessmentEntity assessment : assessmentEntities) {
                    if (courseID == assessment.getCourseID()) {
                        assessmentList.add(assessment);
                    }
                }
                for (AssessmentEntity item : assessmentList){
                }
                adapter.setAssessments(assessmentList);
            }
        });

        iconCourseDetailNote.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, textCourseDetailNote.getText());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Notes from " + textCourseDetailTitle.getText());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
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
            int courseID = firstIntent.getIntExtra(EXTRA_COURSE_ID, -1);
            Intent intent = new Intent(CourseDetailsActivity.this, CourseAddEditActivity.class);
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT,
                    firstIntent.getSerializableExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT));
            startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);

        } else {

            return super.onOptionsItemSelected(item);

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_COURSE_REQUEST_CODE) {
                Toast.makeText(this, "Course Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CourseDetailsActivity.this, CourseDetailsActivity.class);
                intent.putExtras(data);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (requestCode == AssessmentDetailsActivity.ADD_ASSESSMENT_REQUEST_CODE) {
                AssessmentEntity assessment = (AssessmentEntity) data.getSerializableExtra(
                        AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT);

                assessmentViewModel.insert(assessment);
                setData(data);

                Toast.makeText(this, "Assessment Added!", Toast.LENGTH_SHORT).show();
            } else {
                setData(getIntent());
            }
        }
    }

    private void addAssessment() {
        Intent prevIntent = getIntent();
        CourseEntity course = (CourseEntity) prevIntent.getSerializableExtra(EXTRA_COURSE_OBJECT);
        int courseID = course.getCourseID();

        Intent intent = new Intent(CourseDetailsActivity.this, AssessmentAddEditActivity.class);
        intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_COURSE_ID, courseID);
        intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_OBJECT, course);
        startActivityForResult(intent, AssessmentDetailsActivity.ADD_ASSESSMENT_REQUEST_CODE);
    }

    private void setData(Intent intent) {

        CourseEntity course = (CourseEntity)intent.getSerializableExtra(EXTRA_COURSE_OBJECT);


        String startDate = TrackerUtilities.longToDateString(course.getCourseStartDate().getTime());
        String endDate = TrackerUtilities.longToDateString(course.getCourseEndDate().getTime());

        textCourseDetailTitle.setText(course.getCourseTitle());
        textCourseDetailTitle.setTypeface(null, Typeface.BOLD);
        textCourseDetailStatus.setText(course.getCourseStatus());
        textCourseDetailDates.setText(getString(R.string.detail_dates, startDate, endDate));
        textCourseDetailNote.setText(course.getCourseNote());
        textCourseInstructorName.setText(getString(R.string.course_instructor_name,
                (course.getCourseInstructorName() != null)
                        ? course.getCourseInstructorName() : ""));
        textCourseInstructorNumber.setText(getString(R.string.course_instructor_Number,
                (course.getCourseInstructorNumber() != null)
                        ? course.getCourseInstructorNumber() : ""));
        textCourseInstructorEmail.setText(getString(R.string.course_instructor_Email,
                (course.getCourseInstructorEmail() != null)
                        ? course.getCourseInstructorEmail() : ""));
    }

    @Override
    public void onItemClicked(AssessmentEntity assessment) {
        Intent intent = new Intent(CourseDetailsActivity.this, AssessmentDetailsActivity.class);
        intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT, assessment);
        startActivity(intent);
    }

    @Override
    public void onItemLongClicked(AssessmentEntity assessment) {
        String[] options = {"Create Notification for Due Date",
                "Delete Assessment",
                "Cancel"};

        new AlertDialog.Builder(this)
                .setTitle("Choose an option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent currentCourse = getIntent();
                        String currentCourseTitle = currentCourse.getStringExtra(EXTRA_COURSE_TITLE);

                        if (which == 0) {
                            // Create Notification for Start Date
                            Intent intent = new Intent(CourseDetailsActivity.this, TrackerReceiver.class);
                            String content = assessment.getAssessmentTitle() + " from " + currentCourseTitle + " is due today!";
                            intent.putExtra(TrackerReceiver.EXTRA_NOTIFICATION_CONTENT, content);

                            PendingIntent sender = PendingIntent.getBroadcast(
                                    CourseDetailsActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            AlarmManager alarmManager = (AlarmManager) CourseDetailsActivity.this.getSystemService(
                                    Context.ALARM_SERVICE);

                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    assessment.getAssessmentDueDate().getTime(), sender);
                            Toast.makeText(getApplicationContext(), "Notification created!", Toast.LENGTH_SHORT).show();

                        } else if (which == 1) {
                            // Delete Assessment
                            assessmentViewModel.delete(assessment);
                            //Toast.makeText(CourseDetailsActivity.this, "Assessment Deleted!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Assessment Deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }
}
