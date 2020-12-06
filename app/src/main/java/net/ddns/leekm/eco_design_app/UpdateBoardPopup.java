package net.ddns.leekm.eco_design_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

// 게시글 수정
public class UpdateBoardPopup extends AppCompatActivity {
    String type;
    String pos;
    String board;
    TextInputEditText update_title;
    TextInputEditText update_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_board_popup);

        // TextInputEditText 지정
        update_title = findViewById(R.id.update_title);
        update_text = findViewById(R.id.update_text);

        //데이터 가져오기
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        pos = intent.getStringExtra("pos");
        board = intent.getStringExtra("board");
    }

    // 취소 버튼 클릭
    public void close(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    public void update(View v){
        Intent new_intent = new Intent();
        if(type.equals("mainBoard")){ // 게시물 업데이트
            new_intent.putExtra("result", "Update_Board");
            String url = AppData.SERVER_FULL_URL+"/eco_design/eco_design/updateBoard.jsp";
            String parse_data = null;
            String title_str = update_title.getText().toString();
            String text_str = update_text.getText().toString();
            Pattern pattern = Pattern.compile("[<>+%]");
            if(pattern.matcher(title_str).find() || pattern.matcher(text_str).find()){ // 특수문자 들어있으면 true 리턴
                Toast.makeText(this,"사용 불가능한 특수문자가 포함되어 있습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put("게시글번호", pos);
                contentValues.put("제목", URLEncoder.encode(title_str, "utf-8"));
                contentValues.put("내용", URLEncoder.encode(text_str, "utf-8"));
                contentValues.put("분류", board);
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                Log.i("게시글 업데이트 완료","수정완료");
                Toast.makeText(this,"게시글 수정 완료.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new_intent);
                //액티비티(팝업) 닫기
                finish();
            }
        }
    }
}