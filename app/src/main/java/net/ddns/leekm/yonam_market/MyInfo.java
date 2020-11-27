package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class MyInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //타이틀바 없애기
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_info);
    }

    public void close(View v){
        finish();
    }
    public void update(View v){
        finish();
    }
    public void delete(View v){
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