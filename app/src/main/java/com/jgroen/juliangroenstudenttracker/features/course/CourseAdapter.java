package com.jgroen.juliangroenstudenttracker.features.course;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.utils.TrackerReceiver;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {


    private final CourseViewModel courseViewModel;
    private final LayoutInflater inflater;
    private final Context context;
    private List<CourseEntity> courses;

    public CourseAdapter(Context context, CourseViewModel courseViewModel) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.courseViewModel = courseViewModel;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseListTitle;
        private final TextView courseListStatus;

        private CourseViewHolder(View itemView) {
            super(itemView);

            courseListTitle = itemView.findViewById(R.id.courseListTitle);
            courseListStatus = itemView.findViewById(R.id.courseListStatus);

        }

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

        holder.itemView.setOnClickListener(view -> {
            final CourseEntity current = courses.get(position);
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_ID, current.getCourseID());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_TERM_ID, current.getTermID());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_TITLE, current.getCourseTitle());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_STATUS, current.getCourseStatus());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_START_DATE, current.getCourseStartDate().getTime());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_END_DATE, current.getCourseEndDate().getTime());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_NOTE, current.getCourseNote());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NAME, current.getCourseInstructorName());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_NUMBER, current.getCourseInstructorNumber());
            intent.putExtra(CourseDetailsActivity.EXTRA_COURSE_INSTRUCTOR_EMAIL, current.getCourseInstructorEmail());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {

            //termViewModel.delete(terms.get(position));
            String[] options = {"Create Notification for Start Date",
                    "Create Notification for End Date",
                    "Delete Course",
                    "Cancel"};

            new AlertDialog.Builder(context)
                    .setTitle("Choose an option")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CourseEntity current = courses.get(position);


                            if (which == 0) {
                                // Create Notification for Start Date
                                Intent intent = new Intent(context, TrackerReceiver.class);
                                String content = current.getCourseTitle() + " is starting today!";
                                intent.putExtra(TrackerReceiver.EXTRA_NOTIFICATION_CONTENT, content);

                                PendingIntent sender = PendingIntent.getBroadcast(
                                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                                        Context.ALARM_SERVICE);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,
                                        current.getCourseStartDate().getTime(), sender);
                                Toast.makeText(context, "Notification created!", Toast.LENGTH_SHORT).show();

                            } else if (which == 1) {
                                // Create Notification for End Date

                                Intent intent = new Intent(context, TrackerReceiver.class);
                                String content = current.getCourseTitle() + " is ending today!";
                                intent.putExtra(TrackerReceiver.EXTRA_NOTIFICATION_CONTENT, content);

                                PendingIntent sender = PendingIntent.getBroadcast(
                                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                                        Context.ALARM_SERVICE);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,
                                        current.getCourseEndDate().getTime(), sender);
                                Toast.makeText(context, "Notification created!", Toast.LENGTH_SHORT).show();
                            } else if (which == 2) {
                                //Delete Course
                                courseViewModel.delete(current);
                                Toast.makeText(context, "Course deleted!", Toast.LENGTH_SHORT).show();
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
