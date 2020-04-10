package com.jgroen.juliangroenstudenttracker.database;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jgroen.juliangroenstudenttracker.features.course.CourseDao;
import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;
import com.jgroen.juliangroenstudenttracker.utils.Converters;

@Database(entities = {TermEntity.class, CourseEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TrackerRoomDatabase extends RoomDatabase {

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();

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

        private PopulateDbAsync(TrackerRoomDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();

            termDao.insert(new TermEntity(
                    "Term 1",
                    new GregorianCalendar(2020, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2020, Calendar.AUGUST, 31).getTime()));
            termDao.insert(new TermEntity(
                    "Term 2",
                    new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(),
                    new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime() ));
            termDao.insert(new TermEntity(
                    "Term 3",
                    new GregorianCalendar(2021, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2021, Calendar.AUGUST, 31).getTime() ));

            courseDao.deleteAllCourses();

            courseDao.insert(new CourseEntity(
                    "Course 1",
                    new GregorianCalendar(2020, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2020, Calendar.APRIL, 15).getTime(),
                    "In Progress"));
            courseDao.insert(new CourseEntity(
                    "Course 2",
                    new GregorianCalendar(2020, Calendar.APRIL, 15).getTime(),
                    new GregorianCalendar(2020, Calendar.MAY, 30).getTime(),
                    "Enrolled"));
            courseDao.insert(new CourseEntity(
                    "Course 3",
                    new GregorianCalendar(2020, Calendar.JUNE, 1).getTime(),
                    new GregorianCalendar(2020, Calendar.JULY, 15).getTime(),
                    "Enrolled"));

            return null;
        }
    }
}
