package net.ddns.leekm.yonam_market;

import android.view.View;

class MyItem{
    private String title;
    private String userName;
    private String date;
    private String postNumber;

    public MyItem(String title, String userName, String date, String postNumber) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
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