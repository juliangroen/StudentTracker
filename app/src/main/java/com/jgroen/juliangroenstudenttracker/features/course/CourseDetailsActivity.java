package com.jgroen.juliangroenstudenttracker.features.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

    private TextView textCourseDetailTitle;
    private TextView textCourseDetailDates;
    private TextView textCourseDetailStatus;
    private TextView textCourseDetailNote;
    private TextView textCourseInstructorName;
    private TextView textCourseInstructorNumber;
    private TextView textCourseInstructorEmail;

    private AssessmentViewModel assessmentViewModel;

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
                int courseID = intent.getIntExtra(EXTRA_COURSE_ID, -1);
                List<AssessmentEntity> assessmentList = new ArrayList<AssessmentEntity>();
                for (AssessmentEntity assessment : assessmentEntities) {
                    if (courseID == assessment.getCourseID()) {
                        assessmentList.add(assessment);
                    }
                }
                adapter.setAssessments(assessmentList);
            }
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
                Snackbar.make(findViewById(R.id.activityCourseDetails), "Course Updated!", Snackbar.LENGTH_SHORT).show();
            } else if (requestCode == AssessmentDetailsActivity.ADD_ASSESSMENT_REQUEST_CODE) {
                AssessmentEntity assessment = (AssessmentEntity) data.getSerializableExtra(
                        AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT);

                assessmentViewModel.insert(assessment);
                setData(getIntent());

                Snackbar.make(findViewById(R.id.activityCourseDetails), "Assessment Added!", Snackbar.LENGTH_SHORT).show();
            } else {
                setData(getIntent());
            }
        }
    }

    private void addAssessment() {
        Intent prevIntent = getIntent();
        int courseID = prevIntent.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_ID, -1);

        Intent intent = new Intent(CourseDetailsActivity.this, AssessmentAddEditActivity.class);
        intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_COURSE_ID, courseID);
        startActivityForResult(intent, AssessmentDetailsActivity.ADD_ASSESSMENT_REQUEST_CODE);
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

                        if (which == 0) {
                            // Create Notification for Start Date
                            Intent intent = new Intent(CourseDetailsActivity.this, TrackerReceiver.class);
                            String content = assessment.getAssessmentTitle() + "'s due date is today!";
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
