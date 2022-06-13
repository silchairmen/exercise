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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class eat extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    DatePicker dp;
    EditText  edtEat, edtCal, edtEat2, edtCal2, edtEat3, edtCal3;
    TextView edtCon, edtCon2, edtCon3;
    Button btnE, btnWrite;
    String fileName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.eat);

        dp = (DatePicker) findViewById(R.id.datepicker1);
        edtCon = (TextView) findViewById(R.id.edtContent);
        edtEat =(EditText) findViewById(R.id.edtEat);
        edtCal = (EditText) findViewById(R.id.edtCal);
        edtCon2 = (TextView) findViewById(R.id.edtContent2);
        edtEat2 =(EditText) findViewById(R.id.edtEat2);
        edtCal2 = (EditText) findViewById(R.id.edtCal2);
        edtCon3 = (TextView) findViewById(R.id.edtContent3);
        edtEat3 =(EditText) findViewById(R.id.edtEat3);
        edtCal3 = (EditText) findViewById(R.id.edtCal3);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnE = (Button) findViewById(R.id.BtnE);

        Calendar cal = Calendar.getInstance();//calendar 모듈에서 년, 월, 일 값을 받아 cYear, cMonth, cDay로 저장 해줌
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        myHelper = new myDBHelper(this);

        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);//년, 월, 일 형식에 맞게 filename에 저장해준다.
        String str = readEat(fileName); //테이블을 읽어오는 readEat 함수에 fileName을 넣어 날짜를 통해 테이블을 읽는다.
        String str2 = readEat2(fileName);
        String str3 = readEat3(fileName);
        String str4 = readEat4(fileName);
        String str5 = readEat5(fileName);
        String str6 = readEat6(fileName);
        edtEat.setText(str); //readEat에서 리턴한값을 edtEat textview에 붙인다.
        edtCal.setText(str2);
        edtEat2.setText(str3);
        edtCal2.setText(str4);
        edtEat3.setText(str5);
        edtCal3.setText(str6);
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {//데이트 피커가 바뀔때마다 테이블에 있는 값을 textview에 보여준다.

            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fileName = Integer.toString(i) + "_" + Integer.toString(i1 + 1) + "_" + Integer.toString(i2);
                String str = readEat(fileName);
                String str2  = readEat2(fileName);
                String str3 = readEat3(fileName);
                String str4 = readEat4(fileName);
                String str5 = readEat5(fileName);
                String str6 = readEat6(fileName);
                edtEat.setText(str);
                edtCal.setText(str2); // 아침
                edtEat2.setText(str3);
                edtCal2.setText(str4);// 점심
                edtEat3.setText(str5);
                edtCal3.setText(str6);// 저녁
                btnWrite.setEnabled(true);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {//원하는 날짜와 식사 시간에 해당하는 식단 내용을 수정해주는 역할

            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE myEat SET Eat = '"
                        + edtEat.getText().toString()
                        +"',Eatcal = '"
                        + edtCal.getText().toString()
                        + "'WHERE Date = '" + fileName + "'AND EatContent ='아침';"
                );
                sqlDB.execSQL("UPDATE myEat SET Eat = '"
                        + edtEat2.getText().toString()
                        +"',Eatcal = '"
                        + edtCal2.getText().toString()
                        + "'WHERE Date = '" + fileName + "'AND EatContent = '점심';"
                );
                sqlDB.execSQL("UPDATE myEat SET Eat = '"
                        + edtEat3.getText().toString()
                        +"',Eatcal = '"
                        + edtCal3.getText().toString()
                        + "'WHERE Date = '" + fileName + "'AND EatContent ='저녁';"
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


    String readEat(String fName){// fileName을 fName으로 받아 원하는 날짜와 식사시간에 있는 식단을 받아와 Eat으로 리턴해준다.
        String Date = null;
        String Eat = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'아침'"+" ;",null);

            while (cursor.moveToNext()){//테이블에서 원하는 행에 해당되는 값을 cursor를 통해 받아온다.
                Date = cursor.getString(0);
                Eat = cursor.getString(2);
            }
            if (Eat == null){
                edtEat.setHint("음식 입력안함");
                btnWrite.setText("새로 저장");
                if(Date != fName){
                    sqlDB.execSQL("INSERT INTO myEat VALUES('"+fileName+"',"+"'아침'"+","+null+","+null+");");
                    return Eat;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eat;
    }

    String readEat2(String fName){ // fileName을 fName으로 받아 원하는 날짜와 식사시간에 있는 칼로리를 받아와 Eatcal로 리턴해준다.
        String Date = null;
        String Eatcal = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'아침'"+" ;",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);//테이블에서 원하는 행에 해당되는 값을 cursor를 통해 받아온다.
                Eatcal = cursor.getString(3);
            }
            if (Eatcal == null){
                edtCal.setHint("칼로리 입력 안함");
                btnWrite.setText("새로 저장");

            }
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eatcal;
    }

    String readEat3(String fName){
        String Date = null;
        String Eat = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'점심'"+" ;",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                Eat = cursor.getString(2);
            }
            if (Eat == null){
                edtEat2.setHint("음식 입력 안함");
                btnWrite.setText("새로 저장");
                if(Date != fName){
                    sqlDB.execSQL("INSERT INTO myEat VALUES('"+fileName+"',"+"'점심'"+","+null+","+null+");");
                    return Eat;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eat;
    }

    String readEat4(String fName){
        String Date = null;
        String Eatcal = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'점심'"+" ;",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                Eatcal = cursor.getString(3);
            }
            if (Eatcal == null){
                edtCal2.setHint("칼로리 입력안함");
                btnWrite.setText("새로 저장");

            }
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eatcal;
    }

    String readEat5(String fName){
        String Date = null;
        String Eat = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'저녁'"+" ;",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                Eat = cursor.getString(2);
            }
            if (Eat == null){
                edtEat3.setHint("음식 입력 안함");
                btnWrite.setText("새로 저장");
                if(Date != fName){
                    sqlDB.execSQL("INSERT INTO myEat VALUES('"+fileName+"',"+"'저녁'"+","+null+","+null+");");
                    return Eat;
                }
            }
            btnWrite.setText("수정하기");
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eat;
    }

    String readEat6(String fName){
        String Date = null;
        String Eatcal = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myEat WHERE Date = '"
                    + fName + "'AND EatContent = "+"'저녁'"+" ;",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                Eatcal = cursor.getString(3);
            }
            if (Eatcal == null){
                edtCal3.setHint("칼로리 입력 안함");
                btnWrite.setText("새로 저장");

            }
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return Eatcal;
    }
}
