package net.ddns.leekm.eco_design_app;

import java.util.ArrayList;

// tomcat의 게시글 model과 동일.
class MyItem {
    private String title;
    private String userName;
    private String date;
    private String postNumber;
    private String mainText;

    private ArrayList<Comment> comment;
    private String imagePath;

    public MyItem(String title, String userName, String date, String postNumber, String imagePath) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.imagePath = imagePath;
        comment = new ArrayList<Comment>();
    }

    public MyItem(String title, String userName, String date, String postNumber, String mainText, String imagePath) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.postNumber = postNumber;
        this.mainText = mainText;
        this.imagePath = imagePath;
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

    public String getImagePath(){ return imagePath;}
}