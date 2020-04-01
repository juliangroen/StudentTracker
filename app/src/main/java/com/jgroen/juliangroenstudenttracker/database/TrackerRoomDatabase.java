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

import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;
import com.jgroen.juliangroenstudenttracker.utils.Converters;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TermEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TrackerRoomDatabase extends RoomDatabase {

    public abstract TermDao termDao();

    private static volatile TrackerRoomDatabase INSTANCE;

    static TrackerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrackerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TrackerRoomDatabase.class, "tracker_database")
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

        private PopulateDbAsync(TrackerRoomDatabase db) {
            termDao = db.termDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();

            TermEntity term = new TermEntity(
                    "Term 1",
                    new GregorianCalendar(2020, Calendar.MARCH, 1).getTime(),
                    new GregorianCalendar(2020, Calendar.AUGUST, 31).getTime() );
            termDao.insert(term);
            return null;
        }
    }
}
