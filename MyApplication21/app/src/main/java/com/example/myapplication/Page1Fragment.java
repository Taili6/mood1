package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import android.media.MediaPlayer;
import android.content.SharedPreferences;

public class Page1Fragment extends Fragment {

    private ImageButton startTestButton, setNotificationButton, resourceButton, ai_assistant_button, muteButton;
    private Calendar notificationTime;
    private ImageView logoImage;

    private boolean isMuted = false;
    private SharedPreferences preferences;
    private BackgroundMusicManager musicManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 載入 fragment 的佈局
        View view = inflater.inflate(R.layout.fragment_page1, container, false);

        // 取得 logo 圖片
        logoImage = view.findViewById(R.id.logo);

        // 取得 SharedPreferences 來存儲靜音狀態
        preferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        isMuted = preferences.getBoolean("isMuted", false); // 讀取靜音狀態
        musicManager = BackgroundMusicManager.getInstance(getActivity());

        // 初始化靜音按鈕
        muteButton = view.findViewById(R.id.mute_button);
        updateMuteButtonUI(); // 根據當前狀態更新 UI

        // 設定按鈕點擊事件
        muteButton.setOnClickListener(v -> {
            isMuted = !isMuted; // 切換靜音狀態
            if (isMuted) {
                musicManager.pauseMusic(); // 暫停音樂
            } else {
                musicManager.resumeMusic(); // 恢復音樂
            }
            updateMuteButtonUI(); // 更新按鈕 UI
            preferences.edit().putBoolean("isMuted", isMuted).apply(); // 存儲狀態
        });

        // 啟動動畫
        startLogoAnimation();
        // 取得按鈕並設定點擊事件
        startTestButton = view.findViewById(R.id.start_test_button);
        startTestButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "開始檢測按鈕被點擊！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AgeSelectionActivity.class);
            startActivity(intent);
        });

        ai_assistant_button = view.findViewById(R.id.ai_assistant_button);
        ai_assistant_button.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "AI聊天按鈕被點擊!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ChatWithAIActivity.class);
            startActivity(intent);
        });

        resourceButton = view.findViewById(R.id.resource_button);
        resourceButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "專業資源連結按鈕被點擊！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ResourceActivity.class);
            startActivity(intent);
        });

        // 設定通知按鈕
        setNotificationButton = view.findViewById(R.id.set_notification_button);
        setNotificationButton.setOnClickListener(v -> showTimePickerDialog());

        view.findViewById(R.id.mood_diary).setOnClickListener(v -> {
            Toast.makeText(getActivity(), "心情日記已被點擊", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MoodCalendarActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.vrtual_robot).setOnClickListener(v -> {
            Toast.makeText(getActivity(), "VirtualRobot 已被點擊", Toast.LENGTH_SHORT).show();

            // 使用 Intent 打開瀏覽器並導向網址
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://18.181.195.109/"));
            startActivity(browserIntent);
        });

        return view;
    }

    /**
     * 顯示時間選擇對話框
     */
    private void showTimePickerDialog() {
        // 使用 TimePickerDialog 讓使用者選擇通知時間
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
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

    /**
     * 設置通知
     */
    private void setNotification(Calendar calendar) {
        // 使用 getActivity() 作為上下文
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getActivity(), "提醒已設置！", Toast.LENGTH_SHORT).show();
        }
    }
    private void startLogoAnimation() {
        // 获取屏幕的宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // 获取 logo 的宽度
        float logoWidth = logoImage.getWidth();

        // 计算从右到左的移动范围，logo 完全从屏幕右边进入到左边
        float startX = screenWidth; // 从右边开始
        float endX = -logoWidth; // 向左移动，直到完全离开屏幕

        // 创建一个 ValueAnimator 来控制 logo 的水平位移
        ObjectAnimator animator = ObjectAnimator.ofFloat(logoImage, "translationX", startX, endX-470);
        animator.setDuration(6000); // 动画持续时间为 2 秒
        animator.setRepeatCount(ValueAnimator.INFINITE); // 无限重复动画
        animator.setRepeatMode(ValueAnimator.RESTART); // 动画完成后重新开始

        // 启动动画
        animator.start();
    }
    private void updateMuteButtonUI() {
        if (isMuted) {
            muteButton.setImageResource(R.drawable.img_15); // 顯示靜音圖標
        } else {
            muteButton.setImageResource(R.drawable.img_14); // 顯示非靜音圖標
        }
    }

}
