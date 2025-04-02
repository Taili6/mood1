package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PersonInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information); // 確保有對應的佈局

        // 接收從 MainActivity 傳遞過來的資料
        String nickname = getIntent().getStringExtra("nickname");
        String birthdate = getIntent().getStringExtra("birthdate");

        // 在 UI 中顯示
        TextView nicknameTextView = findViewById(R.id.nicknameTextView);
        TextView birthdateTextView = findViewById(R.id.birthdateTextView);

        nicknameTextView.setText(nickname);
        birthdateTextView.setText(birthdate);
    }
}
