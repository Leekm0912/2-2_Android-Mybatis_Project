package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MyInfo extends Activity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //타이틀바 없애기
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_info);
        text = findViewById(R.id.text);
        text.setText("작업을 선택해 주세요");
    }

    public void close(View v){
        finish();
    }
    public void update(View v){
        finish();
    }
    public void delete(View v){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}