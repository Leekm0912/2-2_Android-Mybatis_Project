package net.ddns.leekm.yonam_market;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import cz.msebera.android.httpclient.concurrent.FutureCallback;

public class MainActivity extends AppCompatActivity {

    private EditText insertID;
    private EditText insertPW;
    private TextView version_textView;
    private Button login;
    private Button signup;

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

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        new InternetCheck(internet -> { // 서버 연결 체크
            if(internet) {
                login.setEnabled(true);
                signup.setEnabled(true);
            }else{
                Toast.makeText(this, "서버off", Toast.LENGTH_LONG).show();
            }
        });

        version_textView = findViewById(R.id.version);
        version_textView.setText("Version : "+getVersionInfo(this)+"  "); // 버젼 가져오기





        login.setOnClickListener((v)->{
            String url = AppData.SERVER_FULL_URL+"/yonam-market/market/signIn.jsp";
            String parse_data = null;

            String id = insertID.getText().toString();
            String pw = insertPW.getText().toString();
            if(id.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || pw.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) { // 한글이 포함되면.
                Toast.makeText(this,"영문자, 특수문자만 사용 가능합니다.",Toast.LENGTH_SHORT).show();
                return;
            }else if(id.equals("")){
                Toast.makeText(this,"아이디를 입력해 주세요",Toast.LENGTH_SHORT).show();
                return;
            }else if(pw.equals("")){
                Toast.makeText(this,"패스워드를 입력해 주세요",Toast.LENGTH_SHORT).show();
                return;
            }
            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",id);
            contentValues.put("PW",pw);

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

        signup.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this,SignUp.class);
            startActivityForResult(intent,0);//액티비티 띄우기
        });
    }

    public String getVersionInfo(Context context){
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 화면이 다시 보이면 서버연결체크 다시.
        new InternetCheck(internet -> { // 서버 연결 체크
            if(internet) {
                login.setEnabled(true);
                signup.setEnabled(true);
            }else{
                Toast.makeText(this, "서버off", Toast.LENGTH_LONG).show();
            }
        });
    }
}

class InternetCheck extends AsyncTask<Void,Void,Boolean> {
    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) { try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress(AppData.SERVER_IP, Integer.parseInt(AppData.SERVER_PORT)), 1500);
        sock.close();
        Log.i("====================서버연결 성공======================","");
        return true;
    } catch (IOException e) { e.printStackTrace(); return false; } }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}