package com.jgroen.juliangroenstudenttracker.features.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.database.TrackerRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private TrackerRepository repository;
    private LiveData<List<AssessmentEntity>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);

        repository = new TrackerRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

    public void insert(AssessmentEntity assessmentEntity) {
        repository.insert(assessmentEntity);
    }

    public void update(AssessmentEntity assessmentEntity) {
        repository.update(assessmentEntity);
    }

    public void delete(AssessmentEntity assessmentEntity) {
        repository.delete(assessmentEntity);
    }

    public void deleteAllAssessments() {
        repository.deleteAllAssessments();
    }
}
