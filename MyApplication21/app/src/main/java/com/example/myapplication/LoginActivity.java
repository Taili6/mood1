package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class LoginActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDatabaseHelper(this);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView registerLink = findViewById(R.id.register_link);
        TextView forgotPasswordLink = findViewById(R.id.forgot_password_link);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "請輸入所有字段", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.query(
                            UserDatabaseHelper.TABLE_USERS,
                            new String[]{
                                    UserDatabaseHelper.COLUMN_NICKNAME, // 獲取nickname
                                    UserDatabaseHelper.COLUMN_USERNAME,
                                    UserDatabaseHelper.COLUMN_TEST_RESULT,
                                    UserDatabaseHelper.COLUMN_BEHAVIOR_DATA
                            },
                            UserDatabaseHelper.COLUMN_USERNAME + "=? AND " + UserDatabaseHelper.COLUMN_PASSWORD + "=?",
                            new String[]{username, password},
                            null, null, null
                    );

                    if (cursor.moveToFirst()) {
                        String nickname = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_NICKNAME)); // 獲取nickname
                        String testResult = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_TEST_RESULT));
                        String behaviorData = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_BEHAVIOR_DATA));

                        String personalizedAdvice = generatePersonalizedAdvice(testResult, behaviorData);

                        Toast.makeText(LoginActivity.this, "歡迎回來, " + nickname, Toast.LENGTH_SHORT).show(); // 使用nickname

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("personalizedAdvice", personalizedAdvice);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "用戶名稱或密碼無效", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            }


        });
        } else {
            Log.e("MainActivity", "btnLogin is null");
        }

        // 設置註冊超連結
        String registerText = "沒有帳號？點擊這裡註冊";
        SpannableString spannableString = new SpannableString(registerText);


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 5, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerLink.setText(spannableString);
        registerLink.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private String generatePersonalizedAdvice(String testResult, String behaviorData) {
        return "基於您的測評結果和行為數據，建議您每天進行10分鐘的冥想練習，以提升心理健康。";
    }
}
