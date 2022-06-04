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
import java.util.Date;

public class diary extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    DatePicker dp;
    EditText edtDiary;
    Button btnE, btnWrite;
    String fileName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.diary);

        dp = (DatePicker) findViewById(R.id.datepicker1);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnE = (Button) findViewById(R.id.BtnE);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        myHelper = new myDBHelper(this);

        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);
        String str = readDiary(fileName);
        edtDiary.setText(str);
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fileName = Integer.toString(i) + "_" + Integer.toString(i1 + 1) + "_" + Integer.toString(i2);
                String str = readDiary(fileName);
                edtDiary.setText(str);
                btnWrite.setEnabled(true);
            }
        });
        btnWrite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE myDiary SET content = '"
                        + edtDiary.getText().toString()
                        + "'WHERE diaryDate = '" + fileName + "';");
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

    String readDiary(String fName){
        String diaryDate = null;
        String diaryContent = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myDiary WHERE diaryDate = '"
                    + fName + "';",null);

            while (cursor.moveToNext()){
                diaryDate = cursor.getString(0);
                diaryContent = cursor.getString(1);
            }
            if (diaryContent == null){
                edtDiary.setHint("일기 없음");
                btnWrite.setText("새로 저장");
                if(diaryDate != fName){
                    sqlDB.execSQL("INSERT INTO myDiary VALUES('"+fName+"',"+null+");");
                    return diaryContent;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return diaryContent;
    }

}



