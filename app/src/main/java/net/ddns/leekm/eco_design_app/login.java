package net.ddns.leekm.eco_design_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

// 로그인 화면
public class login extends AppCompatActivity {
    //새로 넣은 TextInputLayout를 insertID로 교체
    private TextInputLayout insertID;
    private TextInputLayout insertPW;
    private Button button;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getEditText()이거 이용해서 EditText로 가는거 같네용
        insertID = findViewById(R.id.insertID);
        insertPW = findViewById(R.id.insertPW);
        button = findViewById(R.id.login_btn);

        //textwatcher 메소드 따로 빼놨어요.
        EditText editTextID = textwatcher(insertID);
        EditText editTextPW = textwatcher(insertPW);

        // 로그인버튼 누를때 동작
        button.setOnClickListener((v) -> {
            String url = AppData.SERVER_FULL_URL+"/eco_design/eco_design/signIn.jsp";
            String parse_data = null;

            String id = editTextID.getText().toString();
            String pw = editTextPW.getText().toString();
            if (id.equals("")) {
                Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (pw.equals("")) {
                Toast.makeText(this, "패스워드를 입력해 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", id);
            contentValues.put("PW", pw);

            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData) getApplication());
            try {
                parse_data = networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1", parse_data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData) getApplication(), parse_data);
            if (p.getNotice().equals("success")) {
                p.setUser();
                Intent intent = new Intent(login.this, login_layout.class);
                startActivityForResult(intent, 0);//액티비티 띄우기
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 화면이 다시 보이면 서버연결체크 다시. 일단 버튼 비활성화 시킴.
        button.setEnabled(false);
        new InternetCheck(internet -> { // 서버 연결 체크
            if(internet) {
                button.setEnabled(true);
            }else{
                button.setEnabled(false);
                Toast.makeText(this, "서버off", Toast.LENGTH_LONG).show();
            }
        });
    }

    public EditText textwatcher(TextInputLayout textInputLayout) {
        //geteditText는 TextInputLayout를 다시 EditText로 바꿔주는 역할입니다.
        EditText editText = textInputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                    textInputLayout.setError("영문자, 특수문자만 사용 가능합니다.");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
        return editText;
    }
}