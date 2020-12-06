package net.ddns.leekm.eco_design_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

// 게시글 작성
public class borad extends AppCompatActivity {
    Button submit;
    TextInputEditText title;
    TextInputEditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borad);
        //db에 담을 제목, 내용
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        submit = findViewById(R.id.submit);
    }
    
    // 게시글 작성버튼 누르면 동작
    public void postSubmit(View v){
        Map<String, String> param = new HashMap<>();
        String url = AppData.SERVER_FULL_URL+"/eco_design/eco_design/postUpload.jsp";
        String parse_data = null;
        ContentValues contentValues = new ContentValues();
        // AsyncTask를 통해 HttpURLConnection 수행.
        try {
            AppData appData = (AppData) getApplication();

            String title_str = title.getText().toString();
            String text_str = text.getText().toString();
            // 허용하지 않을 문자들.
            Pattern pattern = Pattern.compile("[<>+%]");
            if(pattern.matcher(title_str).find() || pattern.matcher(text_str).find()){ // 특수문자 들어있으면 true 리턴
                Toast.makeText(this,"사용 불가능한 특수문자가 포함되어 있습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = getIntent();
            param.put("제목", URLEncoder.encode(title_str, "utf-8"));
            param.put("내용", URLEncoder.encode(text_str, "utf-8"));
            param.put("분류", intent.getStringExtra("분류"));
            param.put("사용자_ID", appData.getUser().getID());

            contentValues.put("제목", URLEncoder.encode(title_str, "utf-8"));
            contentValues.put("내용", URLEncoder.encode(text_str, "utf-8"));
            contentValues.put("분류", intent.getStringExtra("분류"));
            contentValues.put("사용자_ID", appData.getUser().getID());
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "빈칸이 있습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        
        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){ // 서버작업이 정상적으로 처리되면 서버가 success 리턴
            Log.i("삽입완료","삽입완료");
            Toast.makeText(this,"게시물 작성 완료",Toast.LENGTH_SHORT).show();
        }
        else {
            // 서버 작업 결과가 success가 아니면 return 시켜서 액티비티가종료되지 않게 함.
            return;
        }
        finish();
    }
}