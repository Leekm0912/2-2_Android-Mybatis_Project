package net.ddns.leekm.eco_design_app;

// 사용자 정보. Application에 담겨 전역으로 사용될 예정
public class 사용자 {
    private String ID;
    private String PW;
    private String 이름;
    private String 생년월일;
    private String 전화번호;
    private String 주소;


    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getPW() {
        return PW;
    }
    public void setPW(String pW) {
        this.PW = pW;
    }
    public String get전화번호() {
        return 전화번호;
    }
    public void set전화번호(String 전화번호) {
        this.전화번호 = 전화번호;
    }
    public String get이름() {
        return 이름;
    }
    public void set이름(String 이름) {
        this.이름 = 이름;
    }
    public String get생년월일() {
        return 생년월일;
    }
    public void set생년월일(String 생년월일) {
        this.생년월일 = 생년월일;
    }
    public String get주소() {
        return 주소;
    }
    public void set주소(String 주소) {
        this.주소 = 주소;
    }

}