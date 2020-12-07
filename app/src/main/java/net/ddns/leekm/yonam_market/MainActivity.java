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

// 첫 화면.
public class MainActivity extends AppCompatActivity {
    private EditText insertID; // 아이디 입력창
    private EditText insertPW; // 패스워드 입력창
    private TextView version_textView; // 버젼을 보여줄 텍스트뷰
    private Button login; // 로그인 버튼
    private Button signup; //  회원가입 버튼

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
        
        // 로그인 클릭시 동작
        login.setOnClickListener((v)->{
            String url = AppData.SERVER_FULL_URL+"/yonam-market/market/signIn.jsp";
            String parse_data = null;

            String id = insertID.getText().toString();
            String pw = insertPW.getText().toString();
            if(id.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || pw.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) { // 한글이 포함되면.
                Toast.makeText(this,"영문자, 특수문자만 사용 가능합니다.",Toast.LENGTH_SHORT).show();
                return;
            }else if(id.equals("")){ // 아이디가 입력되지 않았다면
                Toast.makeText(this,"아이디를 입력해 주세요",Toast.LENGTH_SHORT).show();
                return;
            }else if(pw.equals("")){ // 패스워드가 입력되지 않았다면
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
            if(p.getNotice().equals("success")){ // 정상 작업시 서버가 success를 리턴해줌
                p.setUser(); // 불러온 사용자 정보를 application 객체에 저장해서 전역정보로 만들어줌.
                Intent intent = new Intent(MainActivity.this,MainMenu.class);
                startActivityForResult(intent,0);//액티비티 띄우기
            }
        });

        // 회원가입 클릭시 동작
        signup.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this,SignUp.class);
            startActivityForResult(intent,0);//액티비티 띄우기
        });
    }
    
    // gradle에서 버젼정보를 불러옴
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
        // 화면이 다시 보이면 서버연결체크 다시. 일단 버튼 비활성화 시킴.
        login.setEnabled(false);
        signup.setEnabled(false);
        new InternetCheck(internet -> { // 서버 연결 체크
            if(internet) {
                login.setEnabled(true);
                signup.setEnabled(true);
            }else{
                login.setEnabled(false); // 왜 인지 여기서는 안되네; 다른 어플에서는 되는데;;
                signup.setEnabled(false);
                Toast.makeText(this, "서버off", Toast.LENGTH_LONG).show();
            }
        });
    }
}