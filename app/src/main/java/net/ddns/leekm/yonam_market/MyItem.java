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
    private ArrayList<Comment> comment;

    public MyItem(String title, String userName, String date, String postNumber) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        comment = new ArrayList<Comment>();
    }

    public MyItem(String title, String userName, String date, String postNumber, String mainText) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.mainText = mainText;
        comment = new ArrayList<Comment>();
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