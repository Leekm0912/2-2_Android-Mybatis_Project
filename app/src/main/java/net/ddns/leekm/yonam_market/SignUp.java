package net.ddns.leekm.yonam_market;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// 회원가입
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

        // 확인 버튼 누를시.
        submit.setOnClickListener((v)->{
            // URL 설정.
            String url = AppData.SERVER_FULL_URL+"/yonam-market/market/signUp.jsp";
            String parse_data = null;

            String id_str = id.getText().toString();
            String pw_str = pw.getText().toString();
            String phone_str = phone.getText().toString();
            if(id_str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || pw_str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) { // 한글이 포함되면.
                Toast.makeText(this,"아이디와 비밀번호는 영문자, 특수문자만 사용 가능합니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!phone_str.matches("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")){ // 전화번호 형식
                Toast.makeText(this,"잘못된 전화번호 형식입니다.",Toast.LENGTH_SHORT).show();
                return;
            }

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
