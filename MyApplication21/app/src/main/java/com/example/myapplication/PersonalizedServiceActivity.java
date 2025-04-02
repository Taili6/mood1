package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalizedServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_service);

        TextView adviceTextView = findViewById(R.id.advice_text_view);

        String personalizedAdvice = getIntent().getStringExtra("personalizedAdvice");
        adviceTextView.setText(personalizedAdvice);
    }
}
