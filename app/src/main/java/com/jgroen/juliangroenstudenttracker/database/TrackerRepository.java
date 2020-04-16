package com.jgroen.juliangroenstudenttracker.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentDao;
import com.jgroen.juliangroenstudenttracker.features.assessment.AssessmentEntity;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDao;
import com.jgroen.juliangroenstudenttracker.features.course.CourseEntity;
import com.jgroen.juliangroenstudenttracker.features.term.TermDao;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;

import java.util.List;

public class TrackerRepository {

    private TermDao termDao;
    private CourseDao courseDao;
    private AssessmentDao assessmentDao;

    private LiveData<List<TermEntity>> allTerms;
    private LiveData<List<CourseEntity>> allCourses;
    private LiveData<List<AssessmentEntity>> allAssessments;

    public TrackerRepository(Application application) {

        TrackerRoomDatabase database = TrackerRoomDatabase.getDatabase(application);

        termDao = database.termDao();
        courseDao = database.courseDao();
        assessmentDao = database.assessmentDao();

        allTerms = termDao.getAllTerms();
        allCourses = courseDao.getAllCourses();
        allAssessments = assessmentDao.getAllAssessments();
    }



    ///////////////
    // TERM CRUD //
    ///////////////

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }

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

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

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


    /////////////////
    // COURSE CRUD //
    /////////////////

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

    public void insert (AssessmentEntity assessmentEntity) {
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessmentEntity);
    }

    private static class InsertAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private AssessmentDao assessmentDao;
        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDao.insert(assessmentEntities[0]);
            return null;
        }
    }

    public void update (AssessmentEntity assessmentEntity) {
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessmentEntity);
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private AssessmentDao assessmentDao;
        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDao.update(assessmentEntities[0]);
            return null;
        }
    }

    public void delete (AssessmentEntity assessmentEntity) {
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessmentEntity);
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private AssessmentDao assessmentDao;
        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDao.delete(assessmentEntities[0]);
            return null;
        }
    }

    public void deleteAllAssessments() {
        new DeleteAllAssessmentsAsyncTask(assessmentDao).execute();
    }

    private static class DeleteAllAssessmentsAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private AssessmentDao assessmentDao;
        private DeleteAllAssessmentsAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDao.deleteAllAssessments();
            return null;
        }
    }
}
