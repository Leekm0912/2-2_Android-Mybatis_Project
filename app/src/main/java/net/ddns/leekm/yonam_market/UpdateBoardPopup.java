package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;

public class UpdateBoardPopup extends AppCompatActivity {
    String type;
    String pos;
    String board;
    TextInputEditText update_title;
    TextInputEditText update_price;
    TextInputEditText update_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_board_popup);

        // TextInputEditText 지정
        update_title = findViewById(R.id.update_title);
        update_price = findViewById(R.id.update_price);
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
            String url = "http://220.66.111.200:8889/yonam-market/market/updateBoard.jsp";
            String parse_data = null;

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("글번호", pos);
            contentValues.put("제목",update_title.getText().toString());
            contentValues.put("내용",update_text.getText().toString());
            contentValues.put("가격",update_price.getText().toString());
            contentValues.put("게시판",board);

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
