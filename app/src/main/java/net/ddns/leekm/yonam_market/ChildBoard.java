package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChildBoard extends AppCompatActivity {
    XmlPullParser parser; // 파서
    ArrayList<MyItem> arrayList; // 파싱해온 값을 저장해줄 리스트
    String xml; // xml의 url
    MyAdapter myAdapter; // 어댑터
    ImageView imageView; // 이미지
    TextView title; // 제목
    TextView desc; // 자기소개
    TextView num; // 글 번호
    TextView date; //글 작성일
    TextView writer; // 글 작성자
    TextView mainText; // 글 본문
    TextInputEditText c_text; // 댓글내용
    Spinner spinner2;
    String board_num;
    MyItem item;
    ListView listView;
    TextView price; //가격

    public void init(){
        String url = "http://220.66.111.200:8889/yonam-market/market/getDetailBoard.jsp";
        ContentValues contentValues = new ContentValues();
        Intent intent = getIntent();
        contentValues.put("게시판",intent.getStringExtra("board"));
        board_num = intent.getStringExtra("board_num");
        contentValues.put("글번호",board_num);
        String parse_data = null;

        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){
            item = p.getMyItem();
        }

        title = findViewById(R.id.board_title);
        num = findViewById(R.id.board_num);
        date = findViewById(R.id.board_date);
        writer = findViewById(R.id.board_writer);
        mainText = findViewById(R.id.mainText);
        price = findViewById(R.id.c_price);

        title.setText(item.getTitle());
        num.setText(item.getPostNumber());
        date.setText(item.getDate());
        writer.setText(item.getUserName());
        mainText.setText(item.getMainText());
        price.setText(item.getPrice());

        // ListView 작업
        listView = findViewById(R.id.comment);
        myAdapter = new MyAdapter(this,R.layout.comment_layout, item.getComment());
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_board);

        init();

        listView.setOnItemClickListener((parent, view, position, l_position)->{
            // 작업안함.
        });

        listView.setOnItemLongClickListener((parent, view, position, id)->{
            Intent new_intent = new Intent(this, Popup.class);
            TextView c_writer = view.findViewById(R.id.c_writer);
            String comment_num = item.getComment().get(position).getComment_num();
            String comment_writer = c_writer.getText().toString();
            AppData appData = (AppData)getApplication();
            if(comment_writer.equals(appData.getUser().get이름())){ // 현재 접속중인 계정의 이름과 댓글의 이름이 같다면
                new_intent.putExtra("data","댓글을 삭제하시겠습니까?");
                new_intent.putExtra("type","Comment");
                new_intent.putExtra("pos",comment_num);
                startActivityForResult(new_intent,0);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void onClick(View v){
        String url = "http://220.66.111.200:8889/yonam-market/market/insertComment.jsp";
        String parse_data = null;

        // AsyncTask를 통해 HttpURLConnection 수행.
        ContentValues contentValues = new ContentValues();
        c_text = findViewById(R.id.comment_input);
        try {
            contentValues.put("내용", c_text.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "댓글을 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }
        AppData appData = (AppData)getApplication();
        contentValues.put("작성자", appData.getUser().getID());
        contentValues.put("게시판_num", board_num);

        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){
            //Intent intent = new Intent(this,MainMenu.class);
            //startActivityForResult(intent,0);//액티비티 띄우기
            Log.i("댓글삽입완료","삽입완료");
        }

        //화면 갱신
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<Comment> list;
        int layout;

        @SuppressLint("ServiceCast")
        public MyAdapter(Context context, int layout, ArrayList<Comment> item){
            this.context = context;
            this.layout = layout;
            this.list = item;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) { return position; }

        /*
         * position : 생성할 항목의 순서값
         * parent : 생성되는 뷰의 부모(지금은 리스트뷰)
         * convertView : 이전에 생성되었던 차일드 뷰(지금은 Layout.xml) 첫 호출시에는 null
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(layout,parent,false);
            }
            TextView c_title = convertView.findViewById(R.id.c_text);
            TextView c_date = convertView.findViewById(R.id.c_date);
            TextView c_writer = convertView.findViewById(R.id.c_writer);
            c_title.setText(list.get(position).get내용());
            c_date.setText(list.get(position).get등록일());
            c_writer.setText(list.get(position).get작성자());

            return convertView;
        }
    }


}
