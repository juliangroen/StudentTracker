package com.jgroen.juliangroenstudenttracker.features.assessment;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private final AssessmentViewModel assessmentViewModel;
    private final LayoutInflater inflater;
    private final Context context;
    private List<AssessmentEntity> assessments;

    public AssessmentAdapter(Context context, AssessmentViewModel assessmentViewModel) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.assessmentViewModel = assessmentViewModel;
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentListType;
        private final TextView assessmentListTitle;
        private final TextView assessmentListDueDate;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);

            assessmentListType = itemView.findViewById(R.id.assessmentListType);
            assessmentListTitle = itemView.findViewById(R.id.assessmentListTitle);
            assessmentListDueDate = itemView.findViewById(R.id.assessmentListDueDate);
        }
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (assessments != null) {
            AssessmentEntity current = assessments.get(position);
            String type = current.getAssessmentType().substring(0,1);
            holder.assessmentListType.setText(type);
            holder.assessmentListType.setTypeface(null, Typeface.BOLD);
            //holder.assessmentListType.setText(context.getResources().getString(R.string.assessment_list_item_type, type));
            holder.assessmentListTitle.setText(current.getAssessmentTitle());

            String dueDate = context.getResources().getString(
                R.string.assessment_list_item_due_date,
                    TrackerUtilities.longToDateString(current.getAssessmentDueDate().getTime()));
            holder.assessmentListDueDate.setText(dueDate);
        } else {
            holder.assessmentListType.setText("N/A");
            holder.assessmentListTitle.setText("N/A");
            holder.assessmentListDueDate.setText("N/A");
        }
    }

    public void setAssessments(List<AssessmentEntity> assessmentList) {
        assessments = assessmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (assessments != null)
            return assessments.size();
        else return 0;
    }

}
