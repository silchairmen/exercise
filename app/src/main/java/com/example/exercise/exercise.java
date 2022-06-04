package com.example.exercise;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class exercise extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    DatePicker dp;
    EditText edtEx, edtSet, edtNum;
    Button btnE, btnWrite;
    String fileName;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.exercise);

        dp = (DatePicker) findViewById(R.id.datepicker1);
        edtEx = (EditText) findViewById(R.id.edtEx);
        edtSet =(EditText) findViewById(R.id.edtSet);
        edtNum = (EditText) findViewById(R.id.edtNum);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnE = (Button) findViewById(R.id.BtnE);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        myHelper = new myDBHelper(this);

        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);
        String str = readExerContent(fileName);
        String str2 = readExerSet(fileName);
        String str3 = readExerNum(fileName);
        edtEx.setText(str);
        edtSet.setText(str2);
        edtNum.setText(str3);
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fileName = Integer.toString(i) + "_" + Integer.toString(i1 + 1) + "_" + Integer.toString(i2);
                String str = readExerContent(fileName);
                String str2 = readExerSet(fileName);
                String str3 = readExerNum(fileName);
                edtEx.setText(str);
                edtSet.setText(str2);
                edtSet.setText(str3);
                btnWrite.setEnabled(true);
            }
        });
        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE myExer SET ExerContent = '"
                        + edtEx.getText().toString()
                        + "',ExerSet = '"
                        + edtSet.getText().toString()
                        +"',ExerNum = '"
                        + edtNum.getText().toString()
                        + "'WHERE ExerDate = '" + fileName + "';"
                );
                sqlDB.close();
                btnWrite.setText("수정하기");
                Toast.makeText(getApplicationContext(), "저장됨",Toast.LENGTH_SHORT).show();
            }
        });


        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    String readExerContent(String fName){
        String ExerDate = null;
        String ExerContent = null;

        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT ExerContent FROM myExer WHERE ExerDate = '"
                    + fName + "';",null);

            while (cursor.moveToNext()){
                ExerDate = cursor.getString(0);
                ExerContent = cursor.getString(1);
            }
            if (ExerContent == null){
                edtEx.setHint("운동 종류 입력 안함");
                btnWrite.setText("새로 저장");
                if(ExerDate != fName){
                    sqlDB.execSQL("INSERT INTO myExer VALUES('"+fName+"',"+null+"',"+null+"',"+null+");");
                    return ExerContent;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return ExerContent;
    }

    String readExerSet(String fName){
        String ExerDate = null;
        String ExerSet= null;

        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT ExerSet FROM myExer WHERE ExerDate = '"
                    + fName + "';",null);

            while (cursor.moveToNext()){
                ExerDate = cursor.getString(0);
                ExerSet = cursor.getString(1);
            }
            if (ExerSet == null){
                edtSet.setHint("세트 수 입력 안함");
                btnWrite.setText("새로 저장");
                if(ExerDate != fName){
                    sqlDB.execSQL("INSERT INTO myExer VALUES('"+fName+"',"+null+"',"+null+"',"+null+");");
                    return ExerSet;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return ExerSet;
    }

    String readExerNum(String fName){
        String ExerDate = null;
        String ExerNum = null;

        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT ExerNum FROM myExer WHERE ExerDate = '"
                    + fName + "';",null);

            while (cursor.moveToNext()){
                ExerDate = cursor.getString(0);
                ExerNum = cursor.getString(1);
            }
            if (ExerNum == null){
                edtNum.setHint("세트당 운동 횟수 입력 안함");
                btnWrite.setText("새로 저장");
                if(ExerDate != fName){
                    sqlDB.execSQL("INSERT INTO myExer VALUES('"+fName+"',"+null+"',"+null+"',"+null+");");
                    return ExerNum;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return ExerNum;
    }


}
