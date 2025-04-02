package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AgeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_selection);

        ImageButton adultButton = findViewById(R.id.adult_button);
        ImageButton collegeStudentButton = findViewById(R.id.college_student_button);
        ImageButton teenagerButton = findViewById(R.id.teenager_button);
        ImageButton backButton = findViewById(R.id.Back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgeSelectionActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });

        adultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click for adults
                Intent intent = new Intent(AgeSelectionActivity.this, AdultEvaluationActivity.class);
                startActivity(intent);
            }
        });

        collegeStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click for college students
                Intent intent = new Intent(AgeSelectionActivity.this, CollegeEvaluationActivity.class);
                startActivity(intent);
            }
        });

        teenagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click for teenagers
                Intent intent = new Intent(AgeSelectionActivity.this, JuniorEvaluationActivity.class);
                startActivity(intent);
            }
        });
    }
}
