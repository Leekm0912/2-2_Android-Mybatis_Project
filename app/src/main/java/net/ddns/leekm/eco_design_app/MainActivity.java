package net.ddns.leekm.eco_design_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// B/A게시판이네. 나중에 이름 수정해라
public class MainActivity extends AppCompatActivity {
    // XML문서를 담는 xml스트링
    // XML의 ListView를 저장해둘 listView
    // 전체 내용을 저장할 arrayList 배열
    String xml;
    ListView listView;
    ArrayList<ListItem> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AsyncTask를 상속받는 Task 생성 및 execute로 시작
        Task task = new Task();
        task.execute();

        //배열 만들기
        arrayList = new ArrayList<>();

        //listView를 찾고 리스너를 등록
        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(mItemClickListener);
    }
    //리스너 재정의 눌렀을 때 해당 position의 값을 받고 해당 for문 내에서 tv값이 해당 인덱스와 같은
    //같다면 해당 배열의 title로 간주하여 내부의 link를 인텐트에 넣어 사이트로 넘어감
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            String tv = parent.getAdapter().getItem(position).toString();
            for(int i=0;i<arrayList.size();i++){
                if(tv.equals(i+"")){
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(i).link));
                    startActivity(intent1);
                }
            }

        }

    };

    //배열 안에서 저장될 클래스를 만들어줌
    class ListItem{
        String category;
        String title;
        String link;

        public ListItem(String category, String title, String link){
            this.category = category;
            this.title = title;
            this.link = link;
        }
    }

    //리턴 타입은 String, String타입의 주소를 받아서 URL로 만든 뒤 해당 주소를 connection하여
    //소스를 html에 한줄씩 읽어와 붙여서 저장한다. 이후 html은 .toString()을 이용해 String 타입으로 리턴
    //URL관련 내용을 다룰 때는 try - catch문을 사용해 다루어야한다.
    String DownloadHtml(String addr){
        StringBuilder html = new StringBuilder();
        try{
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 커넥션이 연결시
            if(conn != null){
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    //BufferReader객체를 만들어 내부에 conn으로 connection하여 저장된
                    // conn의 스트림을 버퍼에 저장
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    for(;;){
                        String line = br.readLine();
                        if(line == null){
                            break;
                        }
                        html.append(line);
                    }
                    br.close();
                }
                //연결끊기
                conn.disconnect();
            }
            //에러가 났을시 확인용
        }catch (Exception e){
            return e.getMessage();
        }

        return html.toString();
    }

    //AsyncTask를 상속받는 Task클래스를 재정의해 만들어준다.
    class Task extends AsyncTask<Void, Void, Void> {
        String add;
        //1번 작업
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //3번 작업
        //어뎁터 객체를 생성 후 MainActivity 에 배열을 합친다.
        @Override
        protected void onPostExecute(Void aVoid) {
            MultiAdapter multiAdapter = new MultiAdapter(MainActivity.this, arrayList);
            listView.setAdapter(multiAdapter);
        }
        //2번째로 작업하고 메인에서가 아닌 새로운 서브 스레드 공간으로 만들어져 활동 후 스스로 만든
        //핸들러로 메인으로 작업을 전달
        @Override
        protected Void doInBackground(Void... voids) {
            //우리가 읽어올 xml주소
            add = "https://rss.blog.naver.com/hyuncomu1.xml";
            //주소를 String 타입으로 가져옴
            xml = DownloadHtml(add);
            //인터넷 관련 부분을 다룰 때에는 try - catch 부분을 반드시 사용해야한다.
            try{
                //Document 객체를 생성 및 불러온 XML 을 String 타입으로 저장한 것을 가져와서 읽는다.
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                Document doc = builder.parse(stream);
                //안정화 작업
                doc.getDocumentElement().normalize();
                //요소로 구분하고 itme 태그 부분을 NodeList 내에 배열로 저장
                //즉, xml 내의 <item>...</item> 으로 구성된 노드를 0번 인덱스 부터
                //item - 1 갯수만큼 저장된 배열 생성
                Element order = doc.getDocumentElement();
                NodeList items = order.getElementsByTagName("item");

                //노드의 길이만큼 돌면서 (노드의 길이 = 인덱스 + 1)
                //다시 item 태그 내부를 요소 별로 나누고 이제는 직접 사용할 title 부분과 link 부분을 가져와
                //배열에 저장시킨다.
                //현재 배열의 상태는
                //배열 index0 / index1 /... /
                //index 0 내부 ListItem 클래스 = title 과 link 를 저장할 수 있는 상태
                for(int i=0;i<items.getLength();i++){
                    Node node = items.item(i);
                    Element element = (Element) node;
                    //title 추출
                    NodeList cate = element.getElementsByTagName("category");
                    String category = cate.item(0).getChildNodes().item(0).getNodeValue();
                    //if(category.equals(value)){
                    NodeList title = element.getElementsByTagName("title");
                    String text = title.item(0).getChildNodes().item(0).getNodeValue();
                    //link 추출
                    NodeList link = element.getElementsByTagName("link");
                    String res = link.item(0).getChildNodes().item(0).getNodeValue();
                    //저장
                    arrayList.add(new ListItem(category, text,res));
                    //

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    //커밋실험용
    //두개를 연결할 BaseAdapter 를 상속 받는 어뎁터 재정의
    class MultiAdapter extends BaseAdapter {
        ArrayList<ListItem> list;
        LayoutInflater inflater;

        public MultiAdapter(Context context, ArrayList<ListItem> list){
            this.list = list;
            inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //만약 convertView 가 null 이라면 즉, 지정되지 않았다면 textlayout 를 넣는다.
            if(convertView == null){
                int res =  R.layout.textlayout;
                convertView = inflater.inflate(res, parent, false);
            }
            //textlayout에 있는 TextView 에 title을 집어 넣는다.
            TextView textView = convertView.findViewById(R.id.text2);
            textView.setText(list.get(position).title);
            return convertView;
        }
    }

}