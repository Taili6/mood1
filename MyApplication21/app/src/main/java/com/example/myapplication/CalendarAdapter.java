package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private MoodDatabaseHelper dbHelper;
    private int[] moodIcons;
    private int daysInMonth;
    private Calendar currentCalendar;
    private int currentMonth;
    private int currentYear;
    private int currentDay;

    public CalendarAdapter(Context context, int daysInMonth) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = new MoodDatabaseHelper(context);
        this.currentCalendar = Calendar.getInstance();
        this.currentMonth = currentCalendar.get(Calendar.MONTH);
        this.currentYear = currentCalendar.get(Calendar.YEAR);
        this.currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        this.daysInMonth = daysInMonth;
        this.moodIcons = new int[daysInMonth];
        loadMoodData();
    }

    private void loadMoodData() {
        for (int i = 0; i < daysInMonth; i++) {
            moodIcons[i] = dbHelper.getMood(i + 1, currentMonth + 1, currentYear);
        }
    }

    @Override
    public int getCount() {
        return daysInMonth;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_day_item, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.dayText);
        ImageView moodIcon = convertView.findViewById(R.id.moodIcon);

        dayText.setText(String.valueOf(position + 1));

        if (moodIcons[position] != 0) {
            moodIcon.setImageResource(moodIcons[position]);
            moodIcon.setVisibility(View.VISIBLE);
        } else {
            moodIcon.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(v -> {

            int selectedDay = position + 1;
            int selectedMonth = Calendar.getInstance().get(Calendar.MONTH);
            int selectedYear = Calendar.getInstance().get(Calendar.YEAR);
            Calendar today = Calendar.getInstance();
            int todayDay = today.get(Calendar.DAY_OF_MONTH);
            if (position + 1 > todayDay) {
                Toast.makeText(context, "不能選擇未來的日期", Toast.LENGTH_SHORT).show();
                return;
            }

            if (moodIcons[position] != 0) {
                showEditMoodDialog(position);
            } else {
                showMoodDialog(position);
            }
        });

        return convertView;
    }

    private void showMoodDialog(int position) {
        int[] icons = {R.drawable.happy, R.drawable.sad, R.drawable.angry, R.drawable.sorrow, R.drawable.joy};
        String[] moods = {"開心", "難過", "憤怒", "悲傷", "快樂"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("選擇心情");
        builder.setItems(moods, (dialog, which) -> {
            moodIcons[position] = icons[which];
            dbHelper.saveMood(position + 1, currentMonth + 1, currentYear, icons[which]);
            notifyDataSetChanged();
        });
        builder.show();
    }

    private void showEditMoodDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("修改或清除心情");
        builder.setMessage("您可以選擇修改或刪除這一天的心情紀錄");
        builder.setPositiveButton("修改", (dialog, which) -> showMoodDialog(position));
        builder.setNegativeButton("清除", (dialog, which) -> {
            moodIcons[position] = 0;
            dbHelper.deleteMood(position + 1, currentMonth + 1, currentYear);
            notifyDataSetChanged();
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }

    public boolean checkMinimumRecords() {
        int count = 0;
        for (int moodIcon : moodIcons) {
            if (moodIcon != 0) {
                count++;
            }
        }
        return count >= 20;
    }

    public void clearAllMoods() {
        for (int i = 0; i < daysInMonth; i++) {
            if (moodIcons[i] != 0) {
                dbHelper.deleteMood(i + 1, currentMonth + 1, currentYear);
                moodIcons[i] = 0;
            }
        }
        notifyDataSetChanged();
    }

    public int getMoodIconAt(int position) {
        return moodIcons[position];
    }

}
