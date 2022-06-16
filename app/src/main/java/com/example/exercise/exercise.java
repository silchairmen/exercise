package com.example.exercise;

import android.content.Context;
import android.content.Intent;
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

public class exercise extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    DatePicker dp;
    EditText edtEx, edtSet, edtNum;
    Button btnE, btnWrite, btnTemp,btnReset;
    String fileName;
    TextView text_temp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.exercise);

        dp = (DatePicker) findViewById(R.id.datepicker1);
        edtEx = (EditText) findViewById(R.id.edtEx);
        edtSet =(EditText) findViewById(R.id.edtSet);
        edtNum = (EditText) findViewById(R.id.edtNum);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnE = (Button) findViewById(R.id.BtnE);
        btnTemp = (Button) findViewById(R.id.btnTmp);
        text_temp = (TextView) findViewById(R.id.textTmp);

        Calendar cal = Calendar.getInstance();//calendar 모듈에서 년, 월, 일 값을 받아 cYear, cMonth, cDay로 저장 해줌
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        myHelper = new myDBHelper(this);

        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1) + "_" + Integer.toString(cDay);//년, 월, 일 형식에 맞게 filename에 저장해준다.
        String str = readExer(fileName);  //테이블을 읽어오는 readExer 함수에 fileName을 넣어 날짜를 통해 테이블을 읽는다.
        btnWrite.setEnabled(true);








        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() { //데이트 피커가 바뀔때마다 테이블에 있는 값을 textview에 보여준다.

            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fileName = Integer.toString(i) + "_" + Integer.toString(i1 + 1) + "_" + Integer.toString(i2);
                String str = readExer(fileName);
                text_temp.setText(str);
                btnWrite.setEnabled(true);
            }
        });
        //임시 저장 버튼에 운동 이름 세트 갯수 저장
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_temp = "\n운동 : " + edtEx.getText().toString() + " - "+ " 개수 : " + edtNum.getText().toString() + " - "+"  세트수 :" + edtSet.getText().toString();

                if(text_temp.getText()==null){
                    text_temp.setText(str_temp);
                }
                else{
                    String str_temp_final = text_temp.getText().toString() + str_temp;
                    text_temp.setText(str_temp_final);
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM myExer WHERE Date = '" + fileName + "';"
                );
                sqlDB.close();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "삭제됨",Toast.LENGTH_SHORT).show();
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE myExer SET ExerContent = '"
                        + text_temp.getText().toString()
                        + "'WHERE Date = '" + fileName + "';"
                );
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "저장됨",Toast.LENGTH_SHORT).show();
            } //원하는 날짜에 있는 운동 내용을 수정해주는 역할
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    String readExer(String fName){ // fileName을 fName으로 받아 원하는 날짜에 있는 운동종류를 받아와 ExerContent로 리턴해준다.
        String Date = null;
        String ExerContent = null;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM myExer WHERE Date = '"
                    + fName + "';",null);

            while (cursor.moveToNext()){
                Date = cursor.getString(0);
                ExerContent = cursor.getString(1);//테이블에서 원하는 행에 해당되는 값을 cursor를 통해 받아온다.
            }
            if (ExerContent == null){
                edtEx.setHint("운동 입력 안함");
                btnWrite.setText("오늘 운동 저장");
                if(Date != fName){
                    sqlDB.execSQL("INSERT INTO myExer VALUES('"+fileName+"',"+null+");"); //데이트 피커가 바뀔때마다 테이블에 바뀐 날짜를 저장해준다. -> 그래야 update를 할때 해당 날짜를 통해 update할수 있기 때문
                    return ExerContent;
                }
            }
            cursor.close();
            sqlDB.close();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),"에러 발생",Toast.LENGTH_SHORT).show();
        }
        return ExerContent;
    }



}
