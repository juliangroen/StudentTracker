package com.jgroen.juliangroenstudenttracker.features.assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDetailsActivity;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

public class AssessmentDetailsActivity extends AppCompatActivity {

    public static final int ADD_ASSESSMENT_REQUEST_CODE = 1;
    public static final int EDIT_ASSESSMENT_REQUEST_CODE = 2;
    public static final String EXTRA_ASSESSMENT_OBJECT = "com.jgroen.juliangroenstudenttracker.EXTRA_ASSESSMENT_OBJECT";
    public static final String EXTRA_ASSESSMENT_COURSE_ID = "com.jgroen.juliangroenstudenttracker.EXTRA_ASSESSMENT_COURSE_ID";

    private TextView textAssessmentDetailTitle;
    private TextView textAssessmentDetailType;
    private TextView textAssessmentDetailDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        textAssessmentDetailTitle = findViewById(R.id.textAssessmentDetailTitle);
        textAssessmentDetailType = findViewById(R.id.textAssessmentDetailType);
        textAssessmentDetailDueDate = findViewById(R.id.textAssessmentDetailDueDate);

        Intent intent = getIntent();

        setData(intent);

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
            Intent intent = new Intent(AssessmentDetailsActivity.this, AssessmentAddEditActivity.class);
            intent.putExtras(firstIntent);
            startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST_CODE);
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setData(Intent intent) {
        AssessmentEntity assessment = (AssessmentEntity)intent.getSerializableExtra(
                EXTRA_ASSESSMENT_OBJECT);

        String dueDate = TrackerUtilities.longToDateString(assessment.getAssessmentDueDate().getTime());

        textAssessmentDetailTitle.setText(assessment.getAssessmentTitle());
        textAssessmentDetailTitle.setTypeface(null, Typeface.BOLD);
        textAssessmentDetailType.setText(getString(R.string.assessment_type, assessment.getAssessmentType()));
        textAssessmentDetailDueDate.setText(getString(R.string.assessment_list_item_due_date, dueDate));
    }
}
