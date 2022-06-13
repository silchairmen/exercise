package com.example.exercise;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class total_diary extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    CalendarView cal;
    EditText totaldiary, totaldiary2;
    Button btnE, btnWrite;
    String fileName;
    int cYear, cMonth, cDay;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.total_diary);

        cal = (CalendarView) findViewById(R.id.calendarView1);
        totaldiary = (EditText) findViewById(R.id.totalDiary);
        totaldiary2 = (EditText) findViewById(R.id.totalDiary2);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnE = (Button) findViewById(R.id.BtnE);



        myHelper = new myDBHelper(this);
        Calendar cal2 = Calendar.getInstance();//calendar 모듈에서 년, 월, 일 값을 받아 cYear, cMonth, cDay로 저장 해줌
        int cYear2 = cal2.get(Calendar.YEAR);
        int cMonth2 = cal2.get(Calendar.MONTH);
        int cDay2 = cal2.get(Calendar.DAY_OF_MONTH);

        fileName = Integer.toString(cYear2) + "_" + Integer.toString(cMonth2 + 1) + "_" + Integer.toString(cDay2);//년, 월, 일 형식에 맞게 filename에 저장해준다.

        String str = readTotal(fileName);//테이블을 읽어오는 readTotal 함수에 fileName을 넣어 날짜를 통해 테이블을 읽는다.
        String str2 = readTotal2(fileName);
        String str3 = readTotal3(fileName);
        totaldiary.setText(str); //readTotal에 리턴 값을 totaldiary textview에 붙인다.
        totaldiary2.setText(str2 + str3);//readTotal2, readTotal3의 리턴 값들을 합쳐 totaldiary2 textview에 붙인다.


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//CalenderView 가 바뀔때마다 테이블에 있는 값을 textview에 보여준다.
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cYear =  year;
                cMonth = month;
                cDay = dayOfMonth;
                fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);

                String str = readTotal(fileName);
                String str2 = readTotal2(fileName);
                String str3 = readTotal3(fileName);
                totaldiary.setText(str); //일기
                totaldiary2.setText(str2 + str3); //운동 및 식단
            }
        });



        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    String readTotal(String fName){ //fileName을 fName으로 받아 myDiary에서 원하는 날짜에 해당하는 일기 내용을 가져와 diaryContent로 리턴해준다.
        String diaryContent = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myDiary WHERE Date = '"
                    + fName + "'AND content is not null;",null); //데이트 피커가 넘어가면 내용을 입력을 안해도 해당 날짜가 테이블에 저장되기 때문에
            //AND content is not null을 안해주면 null값이 보여지게 된다.

            while (cursor.moveToNext()){
                diaryContent = cursor.getString(1);
            }

        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return diaryContent;
    }

    String readTotal2(String fName){ //myExer에서 원하는 날짜에 해당하는 운동종류, 세트수, 세트당 운동 횟수를 가져와 Exerresult로 리턴해준다.
        String Exerresult = "";
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM myExer WHERE Date = '"
                + fName + "'AND ExerContent is NOT NULL;",null);
        while (cursor.moveToNext()){
            Exerresult +="\n"
                    +"운동 기록: "+ cursor.getString(1);
        }
        return Exerresult;
    }

    String readTotal3(String fName){ //myEat에서 원하는 날짜에 해당하는 식사시간, 음식, 칼로리를 가져와 Eatresult로 리턴해준다.
        String Eatresult = "";
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                + fName + "'AND Eat is not null AND Eatcal is not null;",null);
        while (cursor.moveToNext()){
            Eatresult +=
                    " 식사 시간: "+ cursor.getString(1)
                            +", 음식: "
                            + cursor.getString(2)
                            +", 칼로리: "
                            +cursor.getInt(3)
                            + "\n";

        }
        return Eatresult;
    }



}



