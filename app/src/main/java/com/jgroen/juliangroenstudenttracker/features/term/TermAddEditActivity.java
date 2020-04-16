package com.jgroen.juliangroenstudenttracker.features.term;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

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

    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add_edit);

        textTermTitle = findViewById(R.id.textTermTitle);
        dateStartDate = findViewById(R.id.dateStartDate);
        dateEndDate = findViewById(R.id.dateEndDate);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TERM_ID)) {
            setTitle(R.string.activity_term_edit_title);
            textTermTitle.setText(intent.getStringExtra(EXTRA_TERM_TITLE));

            int[] startDateArray = TrackerUtilities.longToDateArray(intent.getLongExtra(EXTRA_TERM_START_DATE, -1));
            int[] endDateArray = TrackerUtilities.longToDateArray(intent.getLongExtra(EXTRA_TERM_END_DATE, -1));

            dateStartDate.updateDate(startDateArray[0], startDateArray[1], startDateArray[2]);
            dateEndDate.updateDate(endDateArray[0], endDateArray[1], endDateArray[2]);
        }

        FloatingActionButton fab = findViewById(R.id.fab_term_save);
        fab.setOnClickListener(view -> {
            saveTerm();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
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
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TERM_ID)) {

            TermEntity term = new TermEntity(title, startDate, endDate);
            term.setTermID(intent.getIntExtra(EXTRA_TERM_ID, -1));
            termViewModel.update(term);

        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, title);
        data.putExtra(EXTRA_TERM_START_DATE, startDate.getTime());
        data.putExtra(EXTRA_TERM_END_DATE, endDate.getTime());
        setResult(RESULT_OK, data);
        finish();
    }
}
