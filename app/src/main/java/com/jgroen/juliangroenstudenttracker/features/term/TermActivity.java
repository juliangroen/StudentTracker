package com.jgroen.juliangroenstudenttracker.features.term;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jgroen.juliangroenstudenttracker.R;

import java.util.Date;
import java.util.List;

public class TermActivity extends AppCompatActivity implements TermAdapter.AdapterCallback {

    public static final int ADD_TERM_REQUEST_CODE = 1;
    public static final int EDIT_TERM_REQUEST_CODE = 2;
    public static final int DELETE_TERM_REQUEST_CODE = 3;

    public static final String EXTRA_TERM_OBJ = "com.jgroen.juliangroenstudenttracker.EXTRA_TERM_OBJ";
    public static final String EXTRA_DELETE_FLAG = "com.jgroen.juliangroenstudenttracker.EXTRA_DELETE_FLAG";
    public static final String EXTRA_NUM_COURSE = "com.jgroen.juliangroenstudenttracker.EXTRA_NUM_COURSE";

    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_term);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermActivity.this, TermAddEditActivity.class);
            startActivityForResult(intent, ADD_TERM_REQUEST_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.termRecyclerView);
        final TermAdapter adapter = new TermAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter.setTerms(termEntities);
            }
        });

    }

    public void removeTerm(TermEntity term) {
        termViewModel.delete(term);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_TERM_REQUEST_CODE) {

                String title = data.getStringExtra(TermAddEditActivity.EXTRA_TERM_TITLE);
                Date startDate = new Date();
                Date endDate = new Date();

                startDate.setTime(data.getLongExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, -1));
                endDate.setTime(data.getLongExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, -1));

                TermEntity term = new TermEntity(title, startDate, endDate);
                termViewModel.insert(term);

                Toast.makeText(this, "Term Saved!", Toast.LENGTH_SHORT).show();

            } else if (requestCode == DELETE_TERM_REQUEST_CODE) {
                if (data.hasExtra(EXTRA_NUM_COURSE)) {
                    if (data.getIntExtra(EXTRA_NUM_COURSE, -1) == 0) {
                        termViewModel.delete((TermEntity)data.getSerializableExtra(TermActivity.EXTRA_TERM_OBJ));
                        Toast.makeText(this, "Term Deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Can't delete a Term with linked courses!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onItemClicked(TermEntity term) {
        Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);
        intent.putExtra(TermActivity.EXTRA_TERM_OBJ, term);
        intent.putExtra(TermAddEditActivity.EXTRA_TERM_ID, term.getTermID());
        intent.putExtra(TermAddEditActivity.EXTRA_TERM_TITLE, term.getTermTitle());
        intent.putExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, term.getTermStartDate().getTime());
        intent.putExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, term.getTermEndDate().getTime());
        startActivity(intent);
    }

    @Override
    public void onItemLongClicked(TermEntity term) {

        String[] options = {"Delete Term", "Cancel"};

        new AlertDialog.Builder(this)
                .setTitle("Choose an option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);

                            intent.putExtra(TermActivity.EXTRA_TERM_OBJ, term);

                            intent.putExtra(TermAddEditActivity.EXTRA_TERM_ID, term.getTermID());
                            intent.putExtra(TermAddEditActivity.EXTRA_TERM_TITLE, term.getTermTitle());
                            intent.putExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, term.getTermStartDate().getTime());
                            intent.putExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, term.getTermEndDate().getTime());

                            intent.putExtra(EXTRA_DELETE_FLAG, true);
                            startActivityForResult(intent, DELETE_TERM_REQUEST_CODE);

                        }
                    }
                }).show();
    }
}
