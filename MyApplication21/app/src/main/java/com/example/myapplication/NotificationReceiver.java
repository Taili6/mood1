package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 獲取通知管理器
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return; // 如果 notificationManager 為空，退出以避免崩潰
        }

        String channelId = "reminder_channel";

        // 創建 NotificationChannel，適用於 Android 8.0 及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "提醒通知",
                    NotificationManager.IMPORTANCE_HIGH // 設置高優先級
            );
            channel.setDescription("Mood Check");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(null, null);  // 默認聲音
            notificationManager.createNotificationChannel(channel);
        }

        // 設置通知點擊後會打開的 Activity
        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                repeatingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 創建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // 使用系統內置的圖標
                .setContentTitle("Mood Check")
                .setContentText("請進行定期的心理健康測評，保持良好的習慣。")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // 確保在 Android 7.1.1 及以下的設備上優先級高
                .setDefaults(NotificationCompat.DEFAULT_ALL);   // 使用默認的聲音、震動和燈光

        // 發送通知
        notificationManager.notify(100, builder.build()); // 檢查是否正確
    }
}
