package com.jgroen.juliangroenstudenttracker.features.assessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDetailsActivity;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

public class AssessmentDetailsActivity extends AppCompatActivity {

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

    private void setData(Intent intent) {
        AssessmentEntity assessment = (AssessmentEntity)intent.getSerializableExtra(
                CourseDetailsActivity.EXTRA_ASSESSMENT_OBJECT);

        String dueDate = TrackerUtilities.longToDateString(assessment.getAssessmentDueDate().getTime());

        textAssessmentDetailTitle.setText(assessment.getAssessmentTitle());
        textAssessmentDetailTitle.setTypeface(null, Typeface.BOLD);
        textAssessmentDetailType.setText(getString(R.string.assessment_type, assessment.getAssessmentType()));
        textAssessmentDetailDueDate.setText(getString(R.string.assessment_list_item_due_date, dueDate));
    }
}
