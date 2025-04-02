package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoodDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moodDB";
    private static final int DATABASE_VERSION = 4; // 升級版本號，確保 onUpgrade 執行
    private static final String TABLE_NAME = "moods";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_MONTH = "month";  // 儲存月份
    private static final String COLUMN_YEAR = "year";    // 儲存年份
    private static final String COLUMN_MOOD = "mood";
    private static final String COLUMN_NOTE = "note"; // 心情筆記欄位

    public MoodDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY + " INTEGER, " +
                COLUMN_MONTH + " INTEGER, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_MOOD + " INTEGER, " +
                COLUMN_NOTE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) { // 確保只在需要時新增 note 欄位
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NOTE + " TEXT");
        }
    }

    public void saveMood(int day, int month, int year, int mood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_MONTH, month);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MOOD, mood);

        // 使用 INSERT OR REPLACE 避免重複資料
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public int getMood(int day, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_MOOD},
                COLUMN_DAY + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_YEAR + "=?",
                new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)},
                null, null, null);

        int mood = 0;
        if (cursor.moveToFirst()) {
            mood = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return mood;
    }

    public void deleteMood(int day, int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_DAY + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_YEAR + "=?",
                new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)});
        db.close();
    }

    public String getMoodNote(int day, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String note = "";
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOTE + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_DAY + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_YEAR + "=?",
                new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)});

        if (cursor.moveToFirst()) {
            note = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return note;
    }

    public void saveMoodNote(int day, int month, int year, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_MONTH, month);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_NOTE, note);

        // 嘗試更新，如果沒有更新到就插入
        int rowsUpdated = db.update(TABLE_NAME, values,
                COLUMN_DAY + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_YEAR + "=?",
                new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)});

        if (rowsUpdated == 0) {
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }
    public void deleteMoodNote(int day, int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, "");  // 清空筆記欄位
        db.update(TABLE_NAME, values, COLUMN_DAY + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_YEAR + "=?",
                new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)});
        db.close();
    }

}
