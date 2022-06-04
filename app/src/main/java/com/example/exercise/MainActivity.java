package com.example.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4;
    TextView timeTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextview = (TextView) findViewById(R.id.timetable);

        /*시간관련 선언!*/
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time2 = simpleDateFormat.format(date);
        String time1 = "TODAY ";
        String time_result = time1.concat(time2);

        timeTextview = (TextView) findViewById(R.id.timetable);
        timeTextview.setText(time_result);


        /*4개의 버튼*/
        btn1 = (Button) findViewById(R.id.work_out);
        btn2 = (Button) findViewById(R.id.food);
        btn3 = (Button) findViewById(R.id.Diary);
        btn4 = (Button) findViewById(R.id.SELF);


        /*동기부여 timer을 설정해서 몇초마다 string에 저장된 동기부여가 나오게 하였도다*/
        Random random = new Random();

        String[] motivate = {
                "\"포기하지 않는 자를 이기는 것은 너무나도 어렵다\"",
                "\"포기하지 말아라. 지금 고통을 겪고 남은 삶은 챔피언으로 살아라.\"",
                "\"어려운 전투일수록 그 승리는 달콤하다.\"",
                "\"당신이 시도하지 않은 슛은 100% 실패로 돌아갈 뿐이다.\"",
                "\"바로 오늘, 당신의 삶의 100%가 남아있다.\"",
                "\"최선을 다하는 자는 후회하지 않는다.\""
        };
        TextView motivate_text;
        motivate_text = (TextView) findViewById(R.id.motivate);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                motivate_text.setText(motivate[random.nextInt(6)]);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt,0,10000);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), exercise.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), eat.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), diary.class);
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), total_diary.class);
                startActivity(intent);
            }
        });
    }
}