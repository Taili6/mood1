package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // 定義資料庫名稱和版本
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 7;

    // 使用者表格
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_BIRTHDATE = "birthdate";
    public static final String COLUMN_ID_LAST4 = "id_last4";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TEST_RESULT = "test_result";
    public static final String COLUMN_BEHAVIOR_DATA = "behavior_data";

    // 心情日記表格
    public static final String TABLE_MOOD_ENTRIES = "mood_entries";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_DATETIME = "datetime";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立使用者表格
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_NICKNAME + " TEXT, " +
                COLUMN_BIRTHDATE + " TEXT, " +
                COLUMN_ID_LAST4 + " TEXT, " +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_TEST_RESULT + " TEXT, " +
                COLUMN_BEHAVIOR_DATA + " TEXT)";

        // 建立心情日記表格
        String createMoodTable = "CREATE TABLE " + TABLE_MOOD_ENTRIES + " (" +
                COLUMN_MOOD + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_TIMESTAMP + " INTEGER, " +
                COLUMN_DATETIME + " TEXT)";
        db.execSQL(createUserTable);
        db.execSQL(createMoodTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOOD_ENTRIES);
        onCreate(db);
    }

    // 插入心情日記到資料庫
    public long insertMoodEntry(MoodEntry moodEntry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOOD, moodEntry.getMood());
        values.put(COLUMN_DESCRIPTION, moodEntry.getDescription());
        values.put(COLUMN_TIMESTAMP, moodEntry.getTimestamp());
        values.put(COLUMN_DATETIME, moodEntry.getFormattedDateTime());

        long result = db.insert(TABLE_MOOD_ENTRIES, null, values);
        db.close();
        return result;
    }

    // 獲取所有心情日記
    public Cursor getAllMoodEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MOOD_ENTRIES + " ORDER BY " + COLUMN_TIMESTAMP + " DESC", null);
    }

    // 更新心情日記（如果需要）
    public int updateMoodEntry(MoodEntry moodEntry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOOD, moodEntry.getMood());
        values.put(COLUMN_DESCRIPTION, moodEntry.getDescription());
        values.put(COLUMN_TIMESTAMP, moodEntry.getTimestamp());
        values.put(COLUMN_DATETIME, moodEntry.getFormattedDateTime());

        // 使用 timestamp 當作唯一識別來更新該條心情記錄
        return db.update(TABLE_MOOD_ENTRIES, values, COLUMN_TIMESTAMP + " = ?", new String[]{String.valueOf(moodEntry.getTimestamp())});
    }

    // 刪除指定的心情日記
    public void deleteMoodEntry(long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOOD_ENTRIES, COLUMN_TIMESTAMP + " = ?", new String[]{String.valueOf(timestamp)});
        db.close();
    }
}
