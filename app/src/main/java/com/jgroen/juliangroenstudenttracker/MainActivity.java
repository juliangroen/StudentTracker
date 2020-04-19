package com.jgroen.juliangroenstudenttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jgroen.juliangroenstudenttracker.features.term.TermActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonMainTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMainTerms = findViewById(R.id.buttonMainTerms);

        setTitle(R.string.activity_main_title);

        buttonMainTerms.setOnClickListener(view -> {
            Intent intent = new Intent(this, TermActivity.class);
            startActivity(intent);
        });
    }
}
