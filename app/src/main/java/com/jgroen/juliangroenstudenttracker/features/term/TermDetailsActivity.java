package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

public class TermDetailsActivity extends AppCompatActivity {

    private TextView textTermDetailTitle;
    private TextView textTermDetailDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        textTermDetailTitle = findViewById(R.id.textTermDetailTitle);
        textTermDetailDates = findViewById(R.id.textTermDetailDates);

        Intent intent = getIntent();
        String startDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, -1));
        String endDate = TrackerUtilities.longToDateString(intent.getLongExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, -1));

        textTermDetailTitle.setText(intent.getStringExtra(TermAddEditActivity.EXTRA_TERM_TITLE));
        textTermDetailDates.setText(getString(R.string.term_detail_dates, startDate, endDate));
        // textTermDetailDates.setText(getString(R.string.term_detail_dates, intent.get));

        FloatingActionButton fab = findViewById(R.id.fabCourseAdd);
        fab.setOnClickListener(view -> {
            addCourse();
        });
    }

    private void addCourse() {
    }
}
