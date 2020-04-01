package com.jgroen.juliangroenstudenttracker.features.term;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jgroen.juliangroenstudenttracker.database.TrackerRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private TrackerRepository repository;
    private LiveData<List<TermEntity>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);

        repository = new TrackerRepository(application);
        allTerms = repository.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }
    public void insert(TermEntity termEntity) {
        repository.insert(termEntity);
    }
    public void update(TermEntity termEntity) {
        repository.update(termEntity);
    }
    public void delete(TermEntity termEntity) {
        repository.delete(termEntity);
    }
    public void deleteAllTerms() {
        repository.deleteAllTerms();
    }
}
