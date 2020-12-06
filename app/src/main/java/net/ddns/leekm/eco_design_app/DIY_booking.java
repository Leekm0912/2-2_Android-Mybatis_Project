package net.ddns.leekm.eco_design_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// DIY 예약 게시판
public class DIY_booking extends AppCompatActivity {
    Button button;
    ArrayList<MyItem> arrayList; // 파싱해온 값을 저장해줄 리스트
    MyAdapter myAdapter; // 어댑터
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_i_y_booking);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.btn1);
        button.setOnClickListener((v) ->{
            Intent intent = new Intent(this,borad.class);
            intent.putExtra("분류","DIY예약");
            startActivity(intent);
        });
        init();
        listView.setOnItemClickListener((parent, view, position, l_position)->{
            // 암시적 호출하기
            Intent intent = new Intent(this,ChildBoard.class);
            TextView textView = view.findViewById(R.id.num);
            Log.i("Intent에 넣을 num값(글번호) : ",textView.getText().toString());
            intent.putExtra("board_num", textView.getText().toString());
            intent.putExtra("board", "DIY예약");
            startActivityForResult(intent,0);//액티비티 띄우기
        });

        listView.setOnItemLongClickListener((parent, view, position, id)->{
            Intent intent = new Intent(this, Popup.class);
            TextView writer = view.findViewById(R.id.writer);
            TextView num = view.findViewById(R.id.num);

            String board_num = writer.getText().toString();
            AppData appData = (AppData)getApplication();
            if(board_num.equals(appData.getUser().get이름()) || appData.getUser().get이름().equals("admin")) { // 현재 접속중인 계정의 이름과 게시글의 이름이 같다면
                intent.putExtra("data", "작업을 선택해 주세요.");
                intent.putExtra("type", "mainBoard");
                intent.putExtra("board", "DIY예약");
                intent.putExtra("pos", num.getText().toString());
                startActivityForResult(intent, 0);
            }
            return true;
        });
    }

    public void init(){
        String url = AppData.SERVER_FULL_URL+"/eco_design/eco_design/getBoard.jsp";
        ContentValues contentValues = new ContentValues();
        contentValues.put("분류","DIY예약");
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
        myAdapter = new MyAdapter(this,R.layout.listview_layout, arrayList);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
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

            title.setText(list.get(position).getTitle());
            num.setText(list.get(position).getPostNumber());
            date.setText(list.get(position).getDate());
            writer.setText(list.get(position).getUserName());

            return convertView;
        }
    }
}