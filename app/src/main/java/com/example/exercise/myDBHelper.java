package com.example.exercise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context){
        super(context, "myDB_final", null, 1);
    }

    public void onCreate(SQLiteDatabase db){

        String Diary = "CREATE TABLE myDiary(Date char(10), content Text);";
        db.execSQL(Diary);

        String Exer ="CREATE TABLE myExer(Date char(10), ExerContent char(500));";
        db.execSQL(Exer);

        String Eat ="CREATE TABLE myEat(Date char(10), EatContent char(10), Eat char(10), Eatcal int(10));";
        db.execSQL(Eat);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String Diary = "DROP TABLE IF EXISTS myDiary";
        db.execSQL(Diary);
        String Exer = "DROP TABLE IF EXISTS myExer";
        db.execSQL(Exer);

        onCreate(db);
    }
}

