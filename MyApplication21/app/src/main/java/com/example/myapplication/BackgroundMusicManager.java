package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusicManager {

    private static BackgroundMusicManager instance; // 單例模式
    private MediaPlayer mediaPlayer;
    private Context context;
    private int currentMusicResId;

    // 私有建構子，確保使用單例模式
    private BackgroundMusicManager(Context context) {
        this.context = context.getApplicationContext();
    }

    // 獲取單例實例
    public static synchronized BackgroundMusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundMusicManager(context);
        }
        return instance;
    }

    // 播放音樂
    public void playMusic(int musicResId, boolean loop) {
        // 如果正在播放其他音樂，先停止並釋放資源
        if (mediaPlayer != null) {
            stopMusic();
        }

        // 初始化 MediaPlayer 並播放新音樂
        mediaPlayer = MediaPlayer.create(context, musicResId);
        currentMusicResId = musicResId;
        mediaPlayer.setLooping(loop);
        mediaPlayer.start();
    }

    // 暫停音樂
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // 恢復音樂
    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // 停止音樂並釋放資源
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 檢查音樂是否正在播放
    public boolean isMusicPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    // 在 Activity 或 Fragment 銷毀時調用，釋放資源
    public void release() {
        if (mediaPlayer != null) {
            stopMusic();
        }
        instance = null;
    }
}
