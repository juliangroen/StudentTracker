package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;

import java.util.Calendar;
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

            Date startDate = new Date();
            Date endDate = new Date();
            Calendar cal = Calendar.getInstance();

            startDate.setTime(Long.parseLong(intent.getStringExtra(EXTRA_TERM_START_DATE), 10));
            cal.setTime(startDate);
            dateStartDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            endDate.setTime(Long.parseLong(intent.getStringExtra(EXTRA_TERM_END_DATE), 10));
            cal.setTime(endDate);
            dateEndDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

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

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TERM_ID)) {

            TermEntity term = new TermEntity(title, startDate, endDate);
            term.setTermID(intent.getIntExtra(EXTRA_TERM_ID, -1));
            termViewModel.update(term);
            finish();

        } else {

            Intent data = new Intent();
            data.putExtra(EXTRA_TERM_TITLE, title);
            data.putExtra(EXTRA_TERM_START_DATE, startDate.getTime());
            data.putExtra(EXTRA_TERM_END_DATE, endDate.getTime());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
