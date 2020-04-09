package com.jgroen.juliangroenstudenttracker.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;

import java.util.List;

public class TrackerRepository {

    private TermDao termDao;
    private CourseDao courseDao;

    private LiveData<List<TermEntity>> allTerms;
    private LiveData<List<CourseEntity>> allCourses;

    public TrackerRepository(Application application) {

        TrackerRoomDatabase database = TrackerRoomDatabase.getDatabase(application);

        termDao = database.termDao();
        courseDao = database.courseDao();

        allTerms = termDao.getAllTerms();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
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

    /////////////////
    // COURSE CRUD //
    /////////////////

    public void insert (CourseEntity courseEntity) {
        new InsertCourseAsyncTask(courseDao).execute(courseEntity);
    }

    private static class InsertCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private CourseDao courseDao;
        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.insert(courseEntities[0]);
            return null;
        }
    }

    public void update (CourseEntity courseEntity) {
        new UpdateCourseAsyncTask(courseDao).execute(courseEntity);
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private CourseDao courseDao;
        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.update(courseEntities[0]);
            return null;
        }
    }

    public void delete (CourseEntity courseEntity) {
        new DeleteCourseAsyncTask(courseDao).execute(courseEntity);
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private CourseDao courseDao;
        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.delete(courseEntities[0]);
            return null;
        }
    }

    public void deleteAllCourses() {
        new DeleteAllCoursesAsyncTask(courseDao).execute();
    }

    private static class DeleteAllCoursesAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private CourseDao courseDao;
        private DeleteAllCoursesAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.deleteAllCourses();
            return null;
        }
    }

}
