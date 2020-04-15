package com.jgroen.juliangroenstudenttracker.features.assessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.util.Date;
import java.util.GregorianCalendar;

public class AssessmentAddEditActivity extends AppCompatActivity {

    private EditText editAssessmentTitle;
    private Spinner spinnerAssessmentType;
    private DatePicker dateAssessmentDueDate;

    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_add_edit);

        editAssessmentTitle = findViewById(R.id.editAssessmentTitle);
        spinnerAssessmentType = findViewById(R.id.spinnerAssessmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssessmentType.setAdapter(adapter);
        dateAssessmentDueDate = findViewById(R.id.dateAssessmentDueDate);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        Intent intent = getIntent();

        if (intent.hasExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT)) {
            setTitle(R.string.activity_assessment_edit_title);
            AssessmentEntity assessment = (AssessmentEntity)intent.getSerializableExtra(
                    AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT);

            String title = assessment.getAssessmentTitle();
            String type = assessment.getAssessmentType();
            int[] dueDate = TrackerUtilities.longToDateArray(
                    assessment.getAssessmentDueDate().getTime());


            editAssessmentTitle.setText(title);

            if (type != null) {
                int typeValue = adapter.getPosition(type);
                spinnerAssessmentType.setSelection(typeValue);
            }

            dateAssessmentDueDate.updateDate(dueDate[0], dueDate[1], dueDate[2]);
        }

        FloatingActionButton fab = findViewById(R.id.fabAssessmentSave);
        fab.setOnClickListener(view -> {
            saveAssessment(intent, adapter);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    private void saveAssessment(Intent intent, ArrayAdapter<CharSequence> adapter) {
        String title = editAssessmentTitle.getText().toString();
        int position = spinnerAssessmentType.getSelectedItemPosition();
        String type = adapter.getItem(position).toString();
        Date dueDate = new GregorianCalendar(
                dateAssessmentDueDate.getYear(),
                dateAssessmentDueDate.getMonth(),
                dateAssessmentDueDate.getDayOfMonth()
        ).getTime();

        AssessmentEntity newAssessment = new AssessmentEntity(-1, title, type, dueDate);
        if (intent.hasExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT)) {
            AssessmentEntity assessment = (AssessmentEntity)intent.getSerializableExtra(
                    AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT);

            newAssessment.setCourseID(assessment.getCourseID());
            newAssessment.setAssessmentID(assessment.getAssessmentID());
        } else if (intent.hasExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_COURSE_ID)){
            int courseID = intent.getIntExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_COURSE_ID, -1);

            newAssessment.setCourseID(courseID);
        }

        Intent data = new Intent();
        data.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_OBJECT, newAssessment);
        setResult(RESULT_OK, data);
        finish();
    }
}
