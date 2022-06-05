package com.example.exercise;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class diary extends AppCompatActivity {
    TextView show_date;
    EditText content;
    Button btn_write,btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        show_date = (TextView) findViewById(R.id.show_date);
        content = (EditText) findViewById(R.id.edtDiary);
        btn_write = (Button) findViewById(R.id.btnWrite);
        btn_write = (Button) findViewById(R.id.btnBack);


        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time2 = simpleDateFormat.format(date);
        String time1 = "         Date ";
        String time_result = time1.concat(time2);

        show_date.setText(time_result); //date+ 날짜를 메모장 첫줄에 저장

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content_result = content.getText().toString(); //일기장 내용 가져오기 content_result

                if(content_result==null){
                }
                else{
                    //db에 추가해주면 되겠죠? @todo
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}



