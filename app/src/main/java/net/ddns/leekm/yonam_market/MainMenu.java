package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {
    // intent에서 request code로 줄 값들.
    final static int BUY = 0;
    final static int SELL = 1;
    final static int MYINFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView textView = findViewById(R.id.info);
        AppData appData = (AppData)getApplication();
        textView.setText("접속계정 : "+appData.getUser().getID() +"\n"+"이름 : "+ appData.getUser().get이름());
    }

    public void buy(View v){
        Intent intent = new Intent(this,MainBoard.class);
        startActivityForResult(intent,BUY);//액티비티 띄우기
    }

    public void sell(View v){
        Intent intent = new Intent(this,SellPage.class);
        startActivityForResult(intent,SELL);//액티비티 띄우기
    }

    public void myInfo(View v){
        Intent intent = new Intent(this, MyInfo.class);
        startActivityForResult(intent,MYINFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MYINFO) {
            if (resultCode == RESULT_OK) {
                finish();
            } else {   // RESULT_CANCEL
                //
            }
        }
    }
}
