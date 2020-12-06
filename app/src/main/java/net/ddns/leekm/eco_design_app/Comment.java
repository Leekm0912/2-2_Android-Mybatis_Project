package net.ddns.leekm.eco_design_app;

// 댓글 클래스
public class Comment {
    private String Comment_num;
    private String 게시판_num;
    private String 등록일;
    private String 작성자;
    private String 내용;

    public String get게시판_num() {
        return 게시판_num;
    }
    public void set게시판_num(String 게시판번호) {
        this.게시판_num = 게시판번호;
    }
    public String get등록일() {
        return 등록일;
    }
    public void set등록일(String 등록일) {
        this.등록일 = 등록일;
    }
    public String get작성자() {
        return 작성자;
    }
    public void set작성자(String iD) {
        작성자 = iD;
    }
    public String get내용() {
        return 내용;
    }
    public void set내용(String 내용) {
        this.내용 = 내용;
    }
    public String getComment_num() {
        return Comment_num;
    }
    public void setComment_num(String comment_num) {
        Comment_num = comment_num;
    }
}
