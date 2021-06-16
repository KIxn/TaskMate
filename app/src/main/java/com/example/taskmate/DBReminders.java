package com.example.taskmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBReminders extends SQLiteOpenHelper {


    public DBReminders( Context context) {
        super(context, "RemindersDb", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE RemindersDb (topic TEXT PRIMARY KEY NOT NULL,desc TEXT,date TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists REMINDERS");
    }

    public boolean insertReminder(String topic,String desc,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("topic",topic);
        contentValues.put("desc",desc);
        contentValues.put("date",date);
        long result = db.insert("RemindersDb",null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteReminder(String topic){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from RemindersDb where topic = ? ",new String[] {topic});
        if(cursor.getCount() > 0){
            long result = db.delete("RemindersDb","topic = ?",new String[] {topic});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getReminders(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from RemindersDb",null);
        return cursor;
    }

}
