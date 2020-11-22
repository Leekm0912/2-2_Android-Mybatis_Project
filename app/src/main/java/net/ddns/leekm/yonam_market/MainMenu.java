package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView textView = findViewById(R.id.info);
        AppData appData = (AppData)getApplication();
        textView.setText("접속계정 : "+appData.getUser().getID() +"\n"+"이름 : "+ appData.getUser().get이름()+"\n"+"ip : "+appData.getUser().getIP());
    }

    public void buy(View v){
        Intent intent = new Intent(this,MainBoard.class);
        startActivityForResult(intent,0);//액티비티 띄우기
    }

    public void sell(View v){
        Intent intent = new Intent(this,SellPage.class);
        startActivityForResult(intent,0);//액티비티 띄우기
    }
}
