package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseActivity;

public class TermActivity extends AppCompatActivity {

    private static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermActivity.this, CourseActivity.class);
            startActivityForResult(intent, NEW_TERM_ACTIVITY_REQUEST_CODE);
        });
    }
}
