package com.jgroen.juliangroenstudenttracker.features.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.database.TrackerRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private TrackerRepository repository;
    private LiveData<List<CourseEntity>> allCourses;
    private LiveData<List<CourseEntity>> allCoursesOfTerm;

    public CourseViewModel(@NonNull Application application) {
        super(application);

        repository = new TrackerRepository(application);
        allCourses = repository.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<CourseEntity>> getAllCoursesForTerm(int termID) {
        allCoursesOfTerm = repository.getAllCoursesForTerm(termID);
        return allCoursesOfTerm;
    }

    public void insert(CourseEntity courseEntity) {
        repository.insert(courseEntity);
    }

    public void update(CourseEntity courseEntity) {
        repository.update(courseEntity);
    }

    public void delete(CourseEntity courseEntity) {
        repository.delete(courseEntity);
    }

    public void deleteAllCourse() {
        repository.deleteAllCourses();
    }
}
