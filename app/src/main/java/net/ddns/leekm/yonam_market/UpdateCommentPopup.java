package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;

public class UpdateCommentPopup extends AppCompatActivity {
    String type;
    String pos;
    String board;
    TextInputEditText update_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_comment_popup);
        // TextInputEditText 지정
        update_comment = findViewById(R.id.update_comment);

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

        new_intent.putExtra("result", "Update_Comment");
        String url = "http://220.66.111.200:8889/yonam-market/market/updateComment.jsp";
        String parse_data = null;

        // AsyncTask를 통해 HttpURLConnection 수행.
        ContentValues contentValues = new ContentValues();
        contentValues.put("comment_num", pos); // 하... 파라미터 주의하자 첫글자 소문자로 줘야함
        //Toast.makeText(this,pos,Toast.LENGTH_SHORT).show();
        contentValues.put("내용",update_comment.getText().toString());
        //contentValues.put("게시판_num",board);

        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){
            Log.i("댓글 업데이트 완료","수정완료");
            Toast.makeText(this,"댓글 수정 완료.",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, new_intent);
            //액티비티(팝업) 닫기
            finish();
        }
    }
}
