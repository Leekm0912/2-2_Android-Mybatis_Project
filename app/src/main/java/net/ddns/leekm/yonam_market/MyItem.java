package net.ddns.leekm.yonam_market;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

class MyItem{
    private String title;
    private String userName;
    private String date;
    private String postNumber;
    private String mainText;
    private String price;

    private ArrayList<Comment> comment;

    public MyItem(String title, String userName, String date, String postNumber, String price) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.price = price;
        comment = new ArrayList<Comment>();
    }

    public MyItem(String title, String userName, String date, String postNumber, String price,String mainText) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.mainText = mainText;
        this.price = price;
        comment = new ArrayList<Comment>();
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment.add(comment);
    }

    public String getMainText() {
        return mainText;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getPostNumber() {
        return postNumber;
    }
}