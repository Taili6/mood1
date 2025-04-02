package com.example.myapplication;

public class MoodEntry {
    private String mood;                // 心情（如「快樂」）
    private String description;          // 事件或心情描述
    private long timestamp;              // 原始時間戳記（毫秒）
    private String formattedDateTime;    // 格式化的日期時間（如「2024/10/07 14:30」）

    // 建構子
    public MoodEntry(String mood, String description, long timestamp, String formattedDateTime) {
        this.mood = mood;
        this.description = description;
        this.timestamp = timestamp;
        this.formattedDateTime = formattedDateTime;
    }

    // Getter 和 Setter 方法
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }

    @Override
    public String toString() {
        return "MoodEntry{" +
                "mood='" + mood + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", formattedDateTime='" + formattedDateTime + '\'' +
                '}';
    }
}
