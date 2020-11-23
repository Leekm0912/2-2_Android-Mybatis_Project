package net.ddns.leekm.yonam_market;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import cz.msebera.android.httpclient.concurrent.FutureCallback;

public class MainActivity extends AppCompatActivity {

    private EditText insertID;
    private EditText insertPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        insertID = findViewById(R.id.insertID);
        insertPW = findViewById(R.id.insertPW);
        Button login = findViewById(R.id.login);
        login.setOnClickListener((v)->{
            String url = "http://leekm.ddns.net:8080/yonam-market/market/signIn.jsp";
            String parse_data = null;

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",insertID.getText().toString());
            contentValues.put("PW",insertPW.getText().toString());

            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                p.setUser();
                Intent intent = new Intent(MainActivity.this,MainMenu.class);
                startActivityForResult(intent,0);//액티비티 띄우기
            }


        });

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this,SignUp.class);
            startActivityForResult(intent,0);//액티비티 띄우기
        });
    }

}
