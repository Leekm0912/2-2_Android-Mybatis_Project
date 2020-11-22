package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ChildBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_board);
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.title);
        textView.setText(intent.getStringExtra("board_num"));
    }
}
