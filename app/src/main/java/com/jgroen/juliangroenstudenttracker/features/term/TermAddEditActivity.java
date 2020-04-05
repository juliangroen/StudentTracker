package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;

import java.util.Date;
import java.util.GregorianCalendar;

public class TermAddEditActivity extends AppCompatActivity {

    public static final String EXTRA_TERM_ID = "com.jgroen.juliangroenstudenttracker.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.jgroen.juliangroenstudenttracker.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.jgroen.juliangroenstudenttracker.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.jgroen.juliangroenstudenttracker.EXTRA_TERM_END_DATE";

    private EditText textTermTitle;
    private DatePicker dateStartDate;
    private DatePicker dateEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add_edit);

        textTermTitle = findViewById(R.id.textTermTitle);
        dateStartDate = findViewById(R.id.dateStartDate);
        dateEndDate = findViewById(R.id.dateEndDate);

        Intent intent = getIntent();

        FloatingActionButton fab = findViewById(R.id.fab_term_save);
        fab.setOnClickListener(view -> {
            saveTerm();
        });
    }

    private void saveTerm() {
        String title = textTermTitle.getText().toString();

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

        if (title.trim().isEmpty()) {
            Snackbar.make(findViewById(R.id.activityTermAddEdit), "Please insert a title", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, title);
        data.putExtra(EXTRA_TERM_START_DATE, startDate.getTime());
        data.putExtra(EXTRA_TERM_END_DATE, endDate.getTime());
        setResult(RESULT_OK, data);
        finish();
    }
}
