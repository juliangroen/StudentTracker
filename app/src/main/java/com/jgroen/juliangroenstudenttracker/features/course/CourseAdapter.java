package com.jgroen.juliangroenstudenttracker.features.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jgroen.juliangroenstudenttracker.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseListTitle;
        private final TextView courseListStatus;

        private CourseViewHolder(View itemView) {
            super(itemView);

            courseListTitle = itemView.findViewById(R.id.courseListTitle);
            courseListStatus = itemView.findViewById(R.id.courseListStatus);

        }

    }

    private final LayoutInflater inflater;
    private final Context context;
    private List<CourseEntity> courses;

    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        if (courses != null) {

            CourseEntity current = courses.get(position);
            holder.courseListTitle.setText(current.getCourseTitle());
            holder.courseListStatus.setText(current.getCourseStatus());

        } else {

            holder.courseListTitle.setText("N/A");
            holder.courseListStatus.setText("N/A");

        }
    }

    public void setCourses(List<CourseEntity> courseList) {
        courses = courseList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null)
            return courses.size();
        else return 0;
    }

}
