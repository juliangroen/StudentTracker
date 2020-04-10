package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseAdapter;
import com.jgroen.juliangroenstudenttracker.features.course.CourseAddEditActivity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDetailsActivity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseViewModel;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

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
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                adapter.setCourses(courseEntities);
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
                Snackbar.make(findViewById(R.id.activityTermDetails), "Term Updated!", Snackbar.LENGTH_SHORT).show();

            } else if (requestCode == CourseDetailsActivity.ADD_COURSE_REQUEST_CODE) {
                String title = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE);
                String status = data.getStringExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS);
                Date startDate = new Date();
                Date endDate = new Date();

                startDate.setTime(data.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, -1));
                endDate.setTime(data.getLongExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, -1));

                CourseEntity course = new CourseEntity(title, startDate, endDate, status);
                courseViewModel.insert(course);

                Snackbar.make(findViewById(R.id.activityTermDetails), "Course Saved!", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            setData(getIntent());

        }
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }*/

    private void setData(Intent intent) {
        String startDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, -1));
        String endDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, -1));

        textTermDetailTitle.setText(intent.getStringExtra(TermAddEditActivity.EXTRA_TERM_TITLE));
        textTermDetailDates.setText(getString(R.string.detail_dates, startDate, endDate));
    }

    private void addCourse() {
        Intent intent = new Intent(TermDetailsActivity.this, CourseAddEditActivity.class);
        startActivityForResult(intent, CourseDetailsActivity.ADD_COURSE_REQUEST_CODE);
    }
}
