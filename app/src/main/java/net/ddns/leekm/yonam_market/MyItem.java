package net.ddns.leekm.yonam_market;

import java.util.ArrayList;

class MyItem{
    private String title;
    private String userName;
    private String date;
    private String postNumber;
    private String mainText;
    private String price;

    private ArrayList<Comment> comment;
    private String imagePath;

    public MyItem(String title, String userName, String date, String postNumber, String price, String imagePath) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.price = price;
        this.imagePath = imagePath;
        comment = new ArrayList<Comment>();
    }

    public MyItem(String title, String userName, String date, String postNumber, String price, String mainText, String imagePath) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.mainText = mainText;
        this.price = price;
        this.imagePath = imagePath;
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

    public String getImagePath(){ return imagePath;}
}