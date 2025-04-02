package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView feedbackTextView;
    private ImageButton backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.score_text_view);
        feedbackTextView = findViewById(R.id.feedback_text_view);
        backToMainButton = findViewById(R.id.back_to_main_button);

        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        String feedback = intent.getStringExtra("FEEDBACK");

        scoreTextView.setText("你的分數是 " + score);
        feedbackTextView.setText(feedback);

        // 設置返回主頁按鈕的點擊事件
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
