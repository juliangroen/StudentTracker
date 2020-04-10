package com.jgroen.juliangroenstudenttracker.features.course;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final CourseEntity current = courses.get(position);
                Intent intent = new Intent(context, CourseDetailsActivity.class);
                intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_ID, current.getCourseID());
                intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE, current.getCourseTitle());
                intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS, current.getCourseStatus());
                intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, current.getCourseStartDate().getTime());
                intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, current.getCourseEndDate().getTime());
                context.startActivity(intent);
            });
        }

    }

    private final CourseViewModel courseViewModel;
    private final LayoutInflater inflater;
    private final Context context;
    private List<CourseEntity> courses;

    public CourseAdapter(Context context, CourseViewModel courseViewModel) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.courseViewModel = courseViewModel;
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

        holder.itemView.setOnLongClickListener(view -> {

            //termViewModel.delete(terms.get(position));
            String[] options = {"Delete Course", "Cancel"};

            new AlertDialog.Builder(context)
                    .setTitle("Choose an option")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                courseViewModel.delete(courses.get(position));
                            }
                        }
                    }).show();
            return true;
        });
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
