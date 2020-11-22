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
        Log.i("=============Test : toString==========",boardtitle.toString());
        Log.i("=============Test : length==========",Integer.toString(boardtitle.size()));
        for(int i=0; i<boardtitle.size(); i++){
            MyItem temp = new MyItem(boardtitle.get(i).text(), writer.get(i).text(), date.get(i).text(), num.get(i).text());
            arrayList.add(temp);
        }
        return arrayList;
    }

}
