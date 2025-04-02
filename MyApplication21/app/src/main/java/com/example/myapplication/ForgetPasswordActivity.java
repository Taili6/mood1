package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPasswordActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        dbHelper = new UserDatabaseHelper(this);

        final EditText birthdateEditText = findViewById(R.id.birthdate);
        final EditText idLast4EditText = findViewById(R.id.id_last4);
        Button resetButton = findViewById(R.id.reset_button);
        ImageButton backButton = findViewById(R.id.Back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthdate = birthdateEditText.getText().toString();
                String idLast4 = idLast4EditText.getText().toString();

                if (birthdate.isEmpty() || idLast4.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "請輸入出生年月日和身分證後四碼", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT " + UserDatabaseHelper.COLUMN_USERNAME + ", " + UserDatabaseHelper.COLUMN_PASSWORD + " FROM " +
                                    UserDatabaseHelper.TABLE_USERS + " WHERE " +
                                    UserDatabaseHelper.COLUMN_BIRTHDATE + " = ? AND " +
                                    UserDatabaseHelper.COLUMN_ID_LAST4 + " = ?",
                            new String[]{birthdate, idLast4});

                    if (cursor.moveToFirst()) {
                        String username = cursor.getString(0);
                        String password = cursor.getString(1);
                        Toast.makeText(ForgetPasswordActivity.this, "帳號: " + username + "\n密碼: " + password, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "找不到符合的用戶資料", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                }
            }
        });
    }
}
