package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.TimePickerDialog;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private BackgroundMusicManager musicManager;

    private Calendar notificationTime; // 確保這裡定義了 notificationTime
    private ImageButton personButton, startTestButton,  setNotificationButton, resourceButton , ai_assistant_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 ViewPager2
        viewPager = findViewById(R.id.view_pager);

        // 創建 Fragment 列表
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Page1Fragment());
        fragmentList.add(new Page2Fragment());


        // 創建並設定 Adapter
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        musicManager = BackgroundMusicManager.getInstance(this);
        musicManager.playMusic(R.raw.serenity, true);

    }


    private void showTimePickerDialog() {
        // 使用 TimePickerDialog 讓使用者選擇通知時間
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(MainActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // 設定通知時間
                    notificationTime = Calendar.getInstance();
                    notificationTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                    notificationTime.set(Calendar.MINUTE, selectedMinute);
                    notificationTime.set(Calendar.SECOND, 0);

                    // 設置通知
                    setNotification(notificationTime);
                }, hour, minute, true);

        timePicker.show();
    }

    private void setNotification(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "提醒已設置！", Toast.LENGTH_SHORT).show();
        }
    }
}
