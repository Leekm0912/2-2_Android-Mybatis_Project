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

// 회원가입창
public class add_log extends AppCompatActivity {
    TextInputLayout name;
    TextInputLayout id;
    TextInputLayout pw;
    TextInputLayout address;
    TextInputLayout birthday;
    TextInputLayout phone;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        // 위젯에 대한 참조.
        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        birthday = findViewById(R.id.birthday);
        phone = findViewById(R.id.phone);

        EditText editTextID = textwatcher_idpw(id);
        EditText editTextPW = textwatcher_idpw(pw);
        EditText editTextPhone = textwatcher_phone(phone);
        EditText editTextName = textwatcher_name(name);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener((v)->{
            // URL 설정.
            String url = AppData.SERVER_FULL_URL+"/eco_design/eco_design/signUp.jsp";
            String parse_data = null;

            String id_str = editTextID.getText().toString();
            String pw_str = editTextPW.getText().toString();
            String phone_str = editTextPhone.getText().toString();

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",id_str);
            contentValues.put("PW",pw_str);
            contentValues.put("이름",editTextName.getText().toString());
            contentValues.put("전화번호",phone_str);
            contentValues.put("주소",address.getEditText().getText().toString());
            contentValues.put("생년월일",birthday.getEditText().getText().toString());
            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){ // 작업이 정상적으로 완료되면 서버에서 success를 보냄.
                Toast.makeText(this,"계정 생성 완료",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public EditText textwatcher_idpw(TextInputLayout textInputLayout) {
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

    public EditText textwatcher_phone(TextInputLayout textInputLayout) {
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
                if(!s.toString().matches("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")){ // 전화번호 형식
                    textInputLayout.setError("전화번호 형식에 맞게 적어주세요");
                }
            }
        });
        return editText;
    }

    public EditText textwatcher_name(TextInputLayout textInputLayout) {
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
                if (!s.toString().contains(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                    textInputLayout.setError("한글로 작성해 주세요");
                }
            }
        });
        return editText;
    }
}
