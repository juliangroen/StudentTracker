package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseAdapter;
import com.jgroen.juliangroenstudenttracker.features.course.CourseAddEditActivity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDetailsActivity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseViewModel;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity {

    private TextView textTermDetailTitle;
    private TextView textTermDetailDates;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        textTermDetailTitle = findViewById(R.id.textTermDetailTitle);
        textTermDetailDates = findViewById(R.id.textTermDetailDates);

        Intent intent = getIntent();

        setData(intent);

        FloatingActionButton fab = findViewById(R.id.fabCourseAdd);
        fab.setOnClickListener(view -> {
            addCourse();
        });

        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        final CourseAdapter adapter = new CourseAdapter(this, courseViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                int termID = intent.getIntExtra(TermAddEditActivity.EXTRA_TERM_ID, -1);
                List<CourseEntity> courseList = new ArrayList<CourseEntity>();
                for (CourseEntity course: courseEntities) {
                    if (termID == course.getTermID())
                        courseList.add(course);
                }
                adapter.setCourses(courseList);

                if (intent.hasExtra(TermActivity.EXTRA_DELETE_FLAG)) {
                    Intent data = new Intent();
                    data.putExtra(TermActivity.EXTRA_NUM_COURSE, courseList.size());
                    data.putExtra(TermActivity.EXTRA_TERM_OBJ, intent.getSerializableExtra(TermActivity.EXTRA_TERM_OBJ));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });


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
            Intent intent = new Intent(TermDetailsActivity.this, TermAddEditActivity.class);
            intent.putExtras(firstIntent);
            startActivityForResult(intent, TermActivity.EDIT_TERM_REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == TermActivity.EDIT_TERM_REQUEST_CODE) {
                setData(data);
                Toast.makeText(this, "Term Updated!", Toast.LENGTH_SHORT).show();

            } else if (requestCode == CourseDetailsActivity.ADD_COURSE_REQUEST_CODE) {
                int termID = data.getIntExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, -1);
                String title = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE);
                String status = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS);
                Date startDate = new Date();
                Date endDate = new Date();
                String note = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_NOTE);
                String name = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NAME);
                String number = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NUMBER);
                String email = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_EMAIL);

                startDate.setTime(data.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, -1));
                endDate.setTime(data.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, -1));

                CourseEntity course = new CourseEntity(termID, title, startDate, endDate, status);
                course.setCourseNote(note);
                course.setCourseInstructorName(name);
                course.setCourseInstructorNumber(number);
                course.setCourseInstructorEmail(email);
                courseViewModel.insert(course);

                Toast.makeText(this, "Course Saved!", Toast.LENGTH_SHORT).show();
            }

        } else {
            setData(getIntent());

        }
    }

    private void setData(Intent intent) {
        String startDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, -1));
        String endDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, -1));

        textTermDetailTitle.setText(intent.getStringExtra(TermAddEditActivity.EXTRA_TERM_TITLE));
        textTermDetailTitle.setTypeface(null, Typeface.BOLD);
        textTermDetailDates.setText(getString(R.string.detail_dates, startDate, endDate));
    }

    private void addCourse() {
        Intent previousIntent = getIntent();
        int termId = previousIntent.getIntExtra(TermAddEditActivity.EXTRA_TERM_ID, -1);

        Intent intent = new Intent(TermDetailsActivity.this, CourseAddEditActivity.class);
        intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, termId);
        startActivityForResult(intent, CourseDetailsActivity.ADD_COURSE_REQUEST_CODE);
    }
}
