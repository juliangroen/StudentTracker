package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;
import com.jgroen.juliangroenstudenttracker.features.course.CourseDetailsActivity;

import java.util.List;

public class TermActivity extends AppCompatActivity {

    private static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermActivity.this, TermAddEditActivity.class);
            startActivityForResult(intent, NEW_TERM_ACTIVITY_REQUEST_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.termRecyclerView);
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter.setTerms(termEntities);
            }
        });
    }
}
