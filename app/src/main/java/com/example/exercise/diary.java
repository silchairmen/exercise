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
        //calendar 모듈에서 년, 월, 일 값을 받아 cYear, cMonth, cDay로 저장 해줌
        myHelper = new myDBHelper(this);
        //myDBHelper 와 연결
        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);//년, 월, 일 형식에 맞게 filename에 저장해준다.
        String str = readDiary(fileName); //테이블을 읽어오는 readDiary 함수에 fileName을 넣어 날짜를 통해 테이블을 읽는다.
        edtDiary.setText(str);  //readDiary에서 리턴한 값을 edtDiary textview에 붙인다.
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fileName = Integer.toString(i) + "_" + Integer.toString(i1 + 1) + "_" + Integer.toString(i2);
                String str = readDiary(fileName);
                edtDiary.setText(str);
                btnWrite.setEnabled(true);
            } //데이트 피커가 바뀔때마다 테이블에 있는 값을 textview에 보여준다.
        });
        btnWrite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE myDiary SET content = '"
                        + edtDiary.getText().toString()
                        + "'WHERE Date = '" + fileName + "';");
                sqlDB.close();
                btnWrite.setText("수정하기");
                Toast.makeText(getApplicationContext(), "저장됨",Toast.LENGTH_SHORT).show();
            } //원하는 날짜에 있는 일기 내용을 수정해주는 역할
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    String readDiary(String fName){
        String Date = null;
        String diaryContent = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myDiary WHERE Date = '"
                    + fName + "';",null); // fileName을 fName으로 받아 원하는 날짜에 있는 일기 내용을 받아와 diaryContent로 리턴해준다.

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                diaryContent = cursor.getString(1); //테이블에서 원하는 행에 해당되는 값을 cursor를 통해 받아온다.
            }
            if (diaryContent == null){
                edtDiary.setHint("일기 없음");
                btnWrite.setText("새로 저장");
                if(Date != fName){
                    sqlDB.execSQL("INSERT INTO myDiary VALUES('"+fName+"',"+null+");");
                    return diaryContent;  //데이트 피커가 바뀔때마다 테이블에 바뀐 날짜를 저장해준다. -> 그래야 update를 할때 해당 날짜를 통해 update할수 있기 때문
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



