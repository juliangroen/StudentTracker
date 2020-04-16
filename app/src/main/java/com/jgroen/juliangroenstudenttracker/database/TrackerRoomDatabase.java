package com.jgroen.juliangroenstudenttracker.database;

import android.content.Context;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeUnit;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentDao;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentEntity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDao;
import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;
import com.jgroen.juliangroenstudenttracker.utils.Converters;
import com.jgroen.juliangroenstudenttracker.utils.TrackerUtilities;

import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TrackerRoomDatabase extends RoomDatabase {

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    private static volatile TrackerRoomDatabase INSTANCE;

    static TrackerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrackerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TrackerRoomDatabase.class, "tracker_database")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;
        private final CourseDao courseDao;
        private final AssessmentDao assessmentDao;

        private PopulateDbAsync(TrackerRoomDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int counter = 0;

            ////////////////////
            // POPULATE TERMS //
            ////////////////////

            termDao.deleteAllTerms();

            TermEntity term1 = new TermEntity(
                    "Term 1",
                    new GregorianCalendar(2020, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2020, Calendar.AUGUST, 31).getTime());

            termDao.insert(term1);

            TermEntity term2 = new TermEntity(
                    "Term 2",
                    new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(),
                    new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime() );
            termDao.insert(term2);

            TermEntity term3 = new TermEntity(
                    "Term 3",
                    new GregorianCalendar(2021, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2021, Calendar.AUGUST, 31).getTime() );

            termDao.insert(term3);

            //////////////////////
            // POPULATE COURSES //
            //////////////////////

            TermEntity[] terms = termDao.loadAllTerms();

            courseDao.deleteAllCourses();

            for (TermEntity term: terms) {

                counter++;

                Calendar startCal = TrackerUtilities.longToCalendar(term.getTermStartDate().getTime());
                Calendar endCal = TrackerUtilities.longToCalendar(term.getTermStartDate().getTime());
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DAY_OF_MONTH, -1);
                CourseEntity course = new CourseEntity(
                        term.getTermID(),
                        "C" + (100 * counter),
                        startCal.getTime(),
                        endCal.getTime(),
                        "In Progress"
                );
                courseDao.insert(course);

                startCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DAY_OF_MONTH, 1);
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DAY_OF_MONTH, -1);
                course = new CourseEntity(
                        term.getTermID(),
                        "C" + ((100 * counter) + 1),
                        startCal.getTime(),
                        endCal.getTime(),
                        "Enrolled"
                );
                courseDao.insert(course);

                startCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DAY_OF_MONTH, 1);
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DAY_OF_MONTH, -1);
                course = new CourseEntity(
                        term.getTermID(),
                        "C" + ((100 * counter) + 2),
                        startCal.getTime(),
                        endCal.getTime(),
                        "Enrolled"
                );
                courseDao.insert(course);
            }

            //////////////////////////
            // POPULATE ASSESSMENTS //
            //////////////////////////

            CourseEntity[] courses = courseDao.loadAllCourses();

            assessmentDao.deleteAllAssessments();

            for (CourseEntity course: courses) {

                Calendar cal = TrackerUtilities.longToCalendar(course.getCourseStartDate().getTime());
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                AssessmentEntity assessment = new AssessmentEntity(
                        course.getCourseID(),
                        "Assessment 1",
                        "Performance",
                        cal.getTime());
                assessmentDao.insert(assessment);

                assessment = new AssessmentEntity(
                        course.getCourseID(),
                        "Assessment 2",
                        "Objective",
                        cal.getTime());
                assessmentDao.insert(assessment);
            }

            return null;
        }
    }
}
