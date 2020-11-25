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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainBoard extends AppCompatActivity {
    XmlPullParser parser; // 파서
    ArrayList<MyItem> arrayList; // 파싱해온 값을 저장해줄 리스트
    String xml; // xml의 url
    MyAdapter myAdapter; // 어댑터
    ImageView imageView; // 이미지
    TextView title; // 제목
    TextView desc; // 자기소개
    Spinner spinner2;
    ListView listView;
    TextView price;

    public void init(){
        String url = "http://220.66.111.200:8889/yonam-market/market/getBoard.jsp";
        ContentValues contentValues = new ContentValues();
        contentValues.put("게시판",spinner2.getSelectedItem().toString());
        String parse_data = null;

        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){
            arrayList = p.getMyItemList();
        }

        // ListView 작업
        listView = findViewById(R.id.list);
        myAdapter = new MyAdapter(this,R.layout.listview_layout,arrayList);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);
        //스피너
        ArrayList arrayList = new ArrayList<>();
        arrayList.add("전자기기");
        arrayList.add("의류");
        arrayList.add("도서");
        arrayList.add("식품");
        arrayList.add("기타");

        spinner2 = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());
        //스피너

        init();

        listView.setOnItemClickListener((parent, view, position, l_position)->{
            // 암시적 호출하기
            Intent intent = new Intent(this,ChildBoard.class);
            TextView textView = view.findViewById(R.id.num);
            Log.i("Intent에 넣을 num값(글번호) : ",textView.getText().toString());
            intent.putExtra("board_num", textView.getText().toString());
            Log.i("Intent에 넣을 게시판종류 : ",spinner2.getSelectedItem().toString());
            intent.putExtra("board", spinner2.getSelectedItem().toString());
            startActivityForResult(intent,0);//액티비티 띄우기
        });

        listView.setOnItemLongClickListener((parent, view, position, id)->{
            Intent intent = new Intent(this, Popup.class);
            TextView writer = view.findViewById(R.id.writer);
            TextView num = view.findViewById(R.id.num);
            String board_num = writer.getText().toString();
            AppData appData = (AppData)getApplication();
            if(board_num.equals(appData.getUser().get이름()) || appData.getUser().get이름().equals("admin")) { // 현재 접속중인 계정의 이름과 게시글의 이름이 같다면
                intent.putExtra("data", "게시물을 삭제하시겠습니까?");
                intent.putExtra("type", "mainBoard");
                intent.putExtra("pos", num.getText().toString());
                startActivityForResult(intent, 0);
            }
            return true;
        });
    }

    public void search(View v){
        String url = "http://220.66.111.200:8889/yonam-market/market/searchBoard.jsp";
        ContentValues contentValues = new ContentValues();
        contentValues.put("게시판",spinner2.getSelectedItem().toString());
        EditText editText = findViewById(R.id.search_text);
        contentValues.put("ptn", editText.getText().toString());
        String parse_data = null;

        NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
        try {
            parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
            Log.i("1",parse_data);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Parse p = new Parse((AppData)getApplication() ,parse_data);
        if(p.getNotice().equals("success")){
            arrayList = p.getMyItemList();
        }

        // ListView 작업
        listView = findViewById(R.id.list);
        myAdapter = new MyAdapter(this,R.layout.listview_layout,arrayList);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String url = "http://220.66.111.200:8889/yonam-market/market/getBoard.jsp";
            ContentValues contentValues = new ContentValues();
            contentValues.put("게시판",spinner2.getSelectedItem().toString());
            String parse_data = null;

            NetworkTask networkTask = new NetworkTask(MainBoard.this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                arrayList = p.getMyItemList();
            }

            // ListView 작업
            ListView listView = findViewById(R.id.list);
            myAdapter = new MyAdapter(MainBoard.this ,R.layout.listview_layout,arrayList);
            listView.setAdapter(myAdapter);
        }

        public void onNothingSelected(AdapterView parent) {

            // Do nothing.

        }

    }

    class MyAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<MyItem> list;
        int layout;

        @SuppressLint("ServiceCast")
        public MyAdapter(Context context, int layout, ArrayList<MyItem> item){
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
            TextView title = convertView.findViewById(R.id.title);
            TextView num = convertView.findViewById(R.id.num);
            TextView date = convertView.findViewById(R.id.date);
            TextView writer = convertView.findViewById(R.id.writer);
            TextView price = convertView.findViewById(R.id.price);
            title.setText(list.get(position).getTitle());
            num.setText(list.get(position).getPostNumber());
            date.setText(list.get(position).getDate());
            writer.setText(list.get(position).getUserName());
            price.setText(list.get(position).getPrice());


            return convertView;
        }
    }


}
