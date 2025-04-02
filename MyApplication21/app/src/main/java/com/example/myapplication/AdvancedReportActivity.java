package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;

public class AdvancedReportActivity extends AppCompatActivity {
    private PieChart pieChart;
    private LineChart lineChart;
    private TextView textViewSummary, textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_report);

        pieChart = findViewById(R.id.pieChart);
        textViewSummary = findViewById(R.id.textViewSummary);


        // 取得從 Intent 傳來的心情數據
        int happy = getIntent().getIntExtra("happy", 0);
        int joy = getIntent().getIntExtra("joy", 0);
        int sad = getIntent().getIntExtra("sad", 0);
        int angry = getIntent().getIntExtra("angry", 0);
        int sorrow = getIntent().getIntExtra("sorrow", 0);
        int[] weeklyMoodData = getIntent().getIntArrayExtra("weeklyMoodData");

        // 設定圓餅圖
        setupPieChart(happy, joy, sad, angry, sorrow);

        // 計算分析數據
        analyzeMood(happy, joy, sad, angry, sorrow);
    }

    private void setupPieChart(int happy, int joy, int sad, int angry, int sorrow) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (happy > 0) entries.add(new PieEntry(happy, "開心 😊"));
        if (joy > 0) entries.add(new PieEntry(joy, "快樂 😃"));
        if (sad > 0) entries.add(new PieEntry(sad, "難過 😢"));
        if (angry > 0) entries.add(new PieEntry(angry, "憤怒 😡"));
        if (sorrow > 0) entries.add(new PieEntry(sorrow, "悲傷 😔"));

        PieDataSet dataSet = new PieDataSet(entries, "心情統計");
        dataSet.setValueTextSize(18f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setColors(new int[]{
                Color.parseColor("#4CAF50"), // 深綠色
                Color.parseColor("#FFC107"), // 深黃色
                Color.parseColor("#2196F3"), // 深藍色
                Color.parseColor("#F44336"), // 深紅色
                Color.parseColor("#9C27B0")  // 深紫色
        });


        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setDescription(null);
        pieChart.animateY(1000);
    }

    private void setupLineChart(int[] weeklyMoodData) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weeklyMoodData.length; i++) {
            entries.add(new Entry(i, weeklyMoodData[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "每週心情變化");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.setDescription(null);
        lineChart.animateX(1000);
    }

    private void analyzeMood(int happy, int joy, int sad, int angry, int sorrow) {
        int[] moodCounts = {happy, joy, sad, angry, sorrow};
        String[] moodLabels = {"開心 😊", "快樂 😃", "難過 😢", "憤怒 😡", "悲傷 😔"};

        int maxMoodIndex = 0;
        for (int i = 1; i < moodCounts.length; i++) {
            if (moodCounts[i] > moodCounts[maxMoodIndex]) {
                maxMoodIndex = i;
            }
        }

        float moodScore = (happy + joy - sad - angry - sorrow) / 5.0f;
        String suggestion = moodScore >= 0 ? "本月整體心情不錯，保持良好的心態！" : "本月負面情緒較多，建議多做放鬆活動。";

        textViewSummary.setText("最常出現的心情: " + moodLabels[maxMoodIndex] + "\n心情平均分數: " + moodScore + "\n" + suggestion);
    }
}