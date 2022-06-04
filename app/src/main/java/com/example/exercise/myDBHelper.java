package com.example.exercise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context){
        super(context, "myDB2", null, 1);
    }

    public void onCreate(SQLiteDatabase db){

        String Diary = "CREATE TABLE myDiary(diaryDate char(10), content varchar(500));";
        db.execSQL(Diary);

        String Exer ="CREATE TABLE myExer(ExerDate char(10), ExerContent char(10), ExerSet char(10), ExerNum char(10));";
        db.execSQL(Exer);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String Diary = "DROP TABLE IF EXISTS myDiary";
        db.execSQL(Diary);
        String Exer = "DROP TABLE IF EXISTS myExer";
        db.execSQL(Exer);

        onCreate(db);
    }
}

