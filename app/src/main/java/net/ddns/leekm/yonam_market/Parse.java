package net.ddns.leekm.yonam_market;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Parse extends Thread{
    private String data;
    AppData appData;
    // 현재 이미지 서버
    public static final String ABSOLUTE_FILE_PATH = "http://leekm0912.i234.me/img/";
    // 이전 이미지 서버
    //public static final String ABSOLUTE_FILE_PATH = "http://220.66.111.200:8889/yonam-market/market/img_upload/img/";

    public Parse(AppData appData, String data){
        this.data = data;
        this.appData = appData;
    }

    // 서버에서 작업 후 성공했으면 <notice>success</noitce> 형태로
    // 실패하면 notice 태그에 원인을 담에서 보내는데, 이를 파싱해주는 메소드.
    public String getNotice(){
        Document doc = Jsoup.parse(data);
        Elements notice = doc.select("notice");
        return notice.text();
    }

    // 로그인 후 불러온 유저정보를 application에 담아 전역변수처럼 사용할수 있게 함.
    public void setUser(){
        Document doc = Jsoup.parse(data);

        appData.getUser().setID(doc.select("ID").text());
        appData.getUser().setIP(doc.select("IP").text());
        appData.getUser().setPW(doc.select("PW").text());
        appData.getUser().set이름(doc.select("NAME").text());
        appData.getUser().set전화번호(doc.select("PHONE").text());
    }

    // MyItem은 tomcat의 게시글 model과 동일.
    // 게시글 목록들을 불러옴.
    public ArrayList<MyItem> getMyItemList(){
        Document doc = Jsoup.parse(data);
        ArrayList<MyItem> arrayList = new ArrayList<>();

        Elements boardtitle = doc.getElementsByTag("boardtitle");
        Elements num = doc.getElementsByTag("num");
        Elements date = doc.getElementsByTag("date");
        Elements writer = doc.getElementsByTag("writer");
        Elements price = doc.getElementsByTag("price");
        Elements filepath = doc.getElementsByTag("filepath");
        for(int i=0; i<boardtitle.size(); i++){
            String s = filepath.get(i).text();
            MyItem temp;
            if(s.equals("")){
                temp = new MyItem(boardtitle.get(i).text(), writer.get(i).text(), date.get(i).text(), num.get(i).text(), price.get(i).text(), null);
            }else{
                temp = new MyItem(boardtitle.get(i).text(), writer.get(i).text(), date.get(i).text(), num.get(i).text(), price.get(i).text(), ABSOLUTE_FILE_PATH + s);
            }

            arrayList.add(temp);
        }
        return arrayList;
    }

    // MyItem은 tomcat의 게시글 model과 동일.
    // 게시글하나의 정보를 불러옴. (해당 게시글의 댓글 포함)
    public MyItem getMyItem(){
        Document doc = Jsoup.parse(data);

        Elements boardtitle = doc.getElementsByTag("boardtitle");
        Elements num = doc.getElementsByTag("num");
        Elements date = doc.getElementsByTag("date");
        Elements writer = doc.getElementsByTag("writer");
        Elements price = doc.getElementsByTag("price");
        Elements text = doc.getElementsByTag("text");
        Elements filepath = doc.getElementsByTag("filepath");

        Elements c_writer = doc.getElementsByTag("c_writer");
        Elements c_date = doc.getElementsByTag("c_date");
        Elements c_text = doc.getElementsByTag("c_text");
        Elements c_num = doc.getElementsByTag("c_num");

        Log.i("=============Comment size",c_writer.toString());
        Log.i("=============Comment",Integer.toString(c_writer.size()));
        String s = filepath.first().text();
        MyItem myItem;
        if(s.equals("")) { // 이미지가 없으면 null을, 있으면 서버경로+이미지 경로를 저장
            myItem = new MyItem(boardtitle.first().text(), writer.first().text(), date.first().text(), num.first().text(),price.first().text(), text.first().text(), null);
        }else {
            myItem = new MyItem(boardtitle.first().text(), writer.first().text(), date.first().text(), num.first().text(),price.first().text(), text.first().text(), ABSOLUTE_FILE_PATH + s);
        }
        
        // 댓글 저장
        for(int i=0; i<c_writer.size(); i++){
            Comment c = new Comment();

            c.set게시판_num(num.first().text());
            c.setComment_num(c_num.get(i).text());
            c.set내용(c_text.get(i).text());
            c.set작성자(c_writer.get(i).text());
            c.set등록일(c_date.get(i).text());
            myItem.setComment(c);
        }

        return myItem;
    }

}
