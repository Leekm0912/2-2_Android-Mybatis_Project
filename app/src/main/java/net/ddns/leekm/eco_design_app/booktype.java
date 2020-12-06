package net.ddns.leekm.eco_design_app;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// 게시판 선택
public class booktype extends AppCompatActivity {
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booktype);

    }

    public void mClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.Consulting_book:
                intent = new Intent(booktype.this,ConsultingBooking.class);
                startActivity(intent);
                break;
            case R.id.DIY_book:
                intent = new Intent(booktype.this,DIY_booking.class);
                startActivity(intent);
                break;
        }
    }
}
