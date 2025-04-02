package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;

public class RegisterActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new UserDatabaseHelper(this);

        // 新增輸入欄位
        final EditText nicknameEditText = findViewById(R.id.nickname); // 暱稱/姓名
        final EditText birthdateEditText = findViewById(R.id.birthdate); // 出生年月日
        final EditText idLast4EditText = findViewById(R.id.id_last4); // 身分證後四碼
        final EditText usernameEditText = findViewById(R.id.username); // 帳號
        final EditText passwordEditText = findViewById(R.id.password); // 密碼

        Button registerButton = findViewById(R.id.register_button);
        ImageButton backButton = findViewById(R.id.Back_button); // 返回按鈕

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                String birthdate = birthdateEditText.getText().toString();
                String idLast4 = idLast4EditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                if (nickname.isEmpty() || birthdate.isEmpty() || idLast4.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "請填寫所有字段", Toast.LENGTH_SHORT).show();
                    return;
                } try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO " + UserDatabaseHelper.TABLE_USERS + " (" +
                                    UserDatabaseHelper.COLUMN_NICKNAME + ", " +
                                    UserDatabaseHelper.COLUMN_BIRTHDATE + ", " +
                                    UserDatabaseHelper.COLUMN_ID_LAST4 + ", " +
                                    UserDatabaseHelper.COLUMN_USERNAME + ", " +
                                    UserDatabaseHelper.COLUMN_PASSWORD + ") VALUES (?, ?, ?, ?, ?)",
                            new Object[]{nickname, birthdate, idLast4, username, password});
                    Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "註冊失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 返回按鈕功能
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
