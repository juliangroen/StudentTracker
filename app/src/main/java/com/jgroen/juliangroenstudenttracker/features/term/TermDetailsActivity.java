package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        FloatingActionButton fab = findViewById(R.id.fabCourseAdd);
        fab.setOnClickListener(view -> {
            addCourse();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_term_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_edit_term) {
            Intent firstIntent = getIntent();
            firstIntent.setClassName("com.jgroen.juliangroenstudenttracker",
                    "com.jgroen.juliangroenstudenttracker.features.term.TermAddEditActivity");
            Intent intent = new Intent(this, TermAddEditActivity.class);
            //startActivity(intent);
            startActivity(firstIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void addCourse() {
    }
}
