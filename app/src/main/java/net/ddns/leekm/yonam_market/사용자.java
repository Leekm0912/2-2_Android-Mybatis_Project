package net.ddns.leekm.yonam_market;

// 사용자 정보. Application에 담겨 전역으로 사용될 예정
public class 사용자 {
    private String ID;
    private String PW;
    private String IP;
    private String 전화번호;
    private String 이름;

    public String get이름() {
        return 이름;
    }
    public void set이름(String _이름) {
        this.이름 = _이름;
    }
    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        this.ID = iD;
    }
    public String getPW() {
        return PW;
    }
    public void setPW(String pW) {
        this.PW = pW;
    }
    public String getIP() {
        return IP;
    }
    public void setIP(String iP) {
        this.IP = iP;
    }
    public String get전화번호() {
        return 전화번호;
    }
    public void set전화번호(String 전화번호) {
        this.전화번호 = 전화번호;
    }
}