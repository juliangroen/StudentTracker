package com.jgroen.juliangroenstudenttracker.features.term;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jgroen.juliangroenstudenttracker.R;

import java.util.Date;
import java.util.List;

public class TermActivity extends AppCompatActivity {

    public static final int ADD_TERM_REQUEST_CODE = 1;
    public static final int EDIT_TERM_REQUEST_CODE = 2;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermActivity.this, TermAddEditActivity.class);
            startActivityForResult(intent, ADD_TERM_REQUEST_CODE);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            String title = data.getStringExtra(TermAddEditActivity.EXTRA_TERM_TITLE);
            Date startDate = new Date();
            Date endDate = new Date();

            startDate.setTime(data.getLongExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, -1));
            endDate.setTime(data.getLongExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, -1));

            TermEntity term = new TermEntity(title, startDate, endDate);
            termViewModel.insert(term);

            Snackbar.make(findViewById(R.id.activityTerm), "Term Saved!", Snackbar.LENGTH_SHORT).show();
        }
    }
}
