package com.jgroen.juliangroenstudenttracker.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;

import java.util.List;

public class TrackerRepository {
    private TermDao termDao;
    private LiveData<List<TermEntity>> allTerms;

    public TrackerRepository(Application application) {
        TrackerRoomDatabase database = TrackerRoomDatabase.getDatabase(application);
        termDao = database.termDao();
        allTerms = termDao.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }

    ///////////////
    // TERM CRUD //
    ///////////////

    public void insert (TermEntity termEntity) {
        new InsertTermAsyncTask(termDao).execute(termEntity);
    }

    private static class InsertTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private TermDao termDao;
        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.insert(termEntities[0]);
            return null;
        }
    }

    public void update (TermEntity termEntity) {
        new UpdateTermAsyncTask(termDao).execute(termEntity);
    }

    private static class UpdateTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private TermDao termDao;
        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.update(termEntities[0]);
            return null;
        }
    }

    public void delete (TermEntity termEntity) {
        new DeleteTermAsyncTask(termDao).execute(termEntity);
    }

    private static class DeleteTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private TermDao termDao;
        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.delete(termEntities[0]);
            return null;
        }
    }

    public void deleteAllTerms() {
        new DeleteAllTermsAsyncTask(termDao).execute();
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private TermDao termDao;
        private DeleteAllTermsAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.deleteAllTerms();
            return null;
        }
    }
}
