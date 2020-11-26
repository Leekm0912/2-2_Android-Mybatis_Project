package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private EditText id;
    private EditText pw;
    private EditText phone;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 위젯에 대한 참조.
        id = (EditText) findViewById(R.id.ID);
        pw = (EditText) findViewById(R.id.PW);
        phone = (EditText) findViewById(R.id.Phone);
        name = (EditText) findViewById(R.id.Name);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener((v)->{
            // URL 설정.
            String url = "http://220.66.111.200:8889/yonam-market/market/signUp.jsp";
            String parse_data = null;

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",id.getText().toString());
            contentValues.put("PW",pw.getText().toString());
            contentValues.put("이름",name.getText().toString());
            contentValues.put("전화번호",phone.getText().toString());
            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                Toast.makeText(this,"계정 생성 완료",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


}
