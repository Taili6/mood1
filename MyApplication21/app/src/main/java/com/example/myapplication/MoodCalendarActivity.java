package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MoodCalendarActivity extends AppCompatActivity {
    private GridView calendarGridView;
    private CalendarAdapter calendarAdapter;
    private TextView monthYearTextView;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_calendar);

        calendarGridView = findViewById(R.id.calendar_grid);
        monthYearTextView = findViewById(R.id.month_year_text_view);

        // 初始化 Calendar 實例
        calendar = Calendar.getInstance();

        // 更新月份顯示
        updateMonthYearDisplay();

        // 設定 GridView
        updateCalendarGrid();

        // 設定按鈕事件
        setupButtons();
    }

    // 更新顯示的月份和年份
    private void updateMonthYearDisplay() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // 月份從 0 開始
        int day = calendar.get(calendar.DAY_OF_MONTH);
        monthYearTextView.setText(String.format("%d年 %d月 %d日", year, month + 1,day));
    }

    // 更新日曆顯示
    private void updateCalendarGrid() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // 計算該月的天數
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(year, month, 1);
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 設定 GridView 及 Adapter
        calendarAdapter = new CalendarAdapter(this, daysInMonth);
        calendarGridView.setAdapter(calendarAdapter);
    }

    // 設定按鈕事件
    private void setupButtons() {
        // 返回按鈕
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish()); // 返回上一頁

        // 生成報告按鈕
        Button btnGenerateReport = findViewById(R.id.btn_generate_report);
        btnGenerateReport.setOnClickListener(v -> generateReport());
    }

    // 生成報告
    private void generateReport() {
        int happyCount = 0, joyCount = 0;
        int sadCount = 0, angryCount = 0, sorrowCount = 0;
        int totalRecords = 0;

        // 遍歷當月所有紀錄
        for (int i = 0; i < calendarAdapter.getCount(); i++) {
            int moodIcon = calendarAdapter.getMoodIconAt(i);
            if (moodIcon != 0) {
                totalRecords++;
                if (moodIcon == R.drawable.happy) happyCount++;
                else if (moodIcon == R.drawable.joy) joyCount++;
                else if (moodIcon == R.drawable.sad) sadCount++;
                else if (moodIcon == R.drawable.angry) angryCount++;
                else if (moodIcon == R.drawable.sorrow) sorrowCount++;
            }
        }

        // 統計正面與負面情緒
        int positiveCount = happyCount + joyCount;
        int negativeCount = sadCount + angryCount + sorrowCount;

        // 生成報告內容
        StringBuilder report = new StringBuilder();
        report.append("當月心情紀錄報告：\n");
        report.append("開心 😄: ").append(happyCount).append(" 次\n");
        report.append("快樂 😊: ").append(joyCount).append(" 次\n");
        report.append("難過 😢: ").append(sadCount).append(" 次\n");
        report.append("憤怒 😡: ").append(angryCount).append(" 次\n");
        report.append("悲傷 😔: ").append(sorrowCount).append(" 次\n\n");

        // 給予鼓勵或建議
        if (totalRecords < 20) {
            report.append("⚠ 當月心情紀錄未達 20 次，請繼續記錄心情！\n\n");
        }

        if (positiveCount > negativeCount) {
            report.append("這個月的正面情緒較多，繼續保持好心情！\n");
        } else if (negativeCount > positiveCount) {
            report.append("這個月的負面情緒較多，試著放鬆心情，做些讓自己開心的事情！\n");
        } else {
            report.append("正面與負面情緒差不多，保持平衡很重要，繼續努力！\n");
        }

        final int finalHappyCount = happyCount;
        final int finalJoyCount = joyCount;
        final int finalSadCount = sadCount;
        final int finalAngryCount = angryCount;
        final int finalSorrowCount = sorrowCount;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("心情報告")
                .setMessage(report.toString())
                .setPositiveButton("確定", null)
                .setNegativeButton("進階報告", (dialog, which) -> {
                    Intent intent = new Intent(MoodCalendarActivity.this, AdvancedReportActivity.class);
                    intent.putExtra("happy", finalHappyCount);
                    intent.putExtra("joy", finalJoyCount);
                    intent.putExtra("sad", finalSadCount);
                    intent.putExtra("angry", finalAngryCount);
                    intent.putExtra("sorrow", finalSorrowCount);
                    startActivity(intent);
                })
                .show();
    }


    private String getMoodString(int moodIcon) {
        if (moodIcon == R.drawable.happy) {
            return "開心";
        } else if (moodIcon == R.drawable.sad) {
            return "難過";
        } else if (moodIcon == R.drawable.angry) {
            return "憤怒";
        } else if (moodIcon == R.drawable.sorrow) {
            return "悲傷";
        } else if (moodIcon == R.drawable.joy) {
            return "快樂";
        } else {
            return "未知";
        }
    }
}
