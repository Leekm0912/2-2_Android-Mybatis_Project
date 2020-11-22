package net.ddns.leekm.yonam_market;

import android.app.Application;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Parse extends Thread{
    private String data;
    AppData appData;

    public Parse(AppData appData, String data){
        this.data = data;
        this.appData = appData;
    }

    public String getNotice(){
        Document doc = Jsoup.parse(data);
        Elements notice = doc.select("notice");
        return notice.text();
    }

    public void setUser(){
        Document doc = Jsoup.parse(data);

        Elements ele = doc.getElementsByTag("item");
        NodeList items;

        appData.getUser().setID(doc.select("ID").text());
        appData.getUser().setIP(doc.select("IP").text());
        appData.getUser().setPW(doc.select("PW").text());
        appData.getUser().set이름(doc.select("NAME").text());
        appData.getUser().set전화번호(doc.select("PHONE").text());
    }

    public ArrayList<MyItem> getMyItemList(){
        Document doc = Jsoup.parse(data);
        ArrayList<MyItem> arrayList = new ArrayList<>();


        Elements boardtitle = doc.getElementsByTag("boardtitle");
        Elements num = doc.getElementsByTag("num");
        Elements date = doc.getElementsByTag("date");
        Elements writer = doc.getElementsByTag("writer");
        Elements price = doc.getElementsByTag("price");
        Log.i("=============Test : toString==========",boardtitle.toString());
        Log.i("=============Test : length==========",Integer.toString(boardtitle.size()));
        for(int i=0; i<boardtitle.size(); i++){
            MyItem temp = new MyItem(boardtitle.get(i).text(), writer.get(i).text(), date.get(i).text(), num.get(i).text(), price.get(i).text());
            arrayList.add(temp);
        }
        return arrayList;
    }

    public MyItem getMyItem(){
        Document doc = Jsoup.parse(data);
        ArrayList<MyItem> arrayList = new ArrayList<>();


        Elements boardtitle = doc.getElementsByTag("boardtitle");
        Elements num = doc.getElementsByTag("num");
        Elements date = doc.getElementsByTag("date");
        Elements writer = doc.getElementsByTag("writer");
        Elements price = doc.getElementsByTag("price");
        Elements text = doc.getElementsByTag("text");
        Elements c_writer = doc.getElementsByTag("c_writer");
        Elements c_date = doc.getElementsByTag("c_date");
        Elements c_text = doc.getElementsByTag("c_text");
        Elements c_num = doc.getElementsByTag("c_num");

        Log.i("=============Comment size",c_writer.toString());
        Log.i("=============Comment",Integer.toString(c_writer.size()));

        MyItem myItem = new MyItem(boardtitle.first().text(), writer.first().text(), date.first().text(), num.first().text(),price.first().text(), text.first().text());
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
