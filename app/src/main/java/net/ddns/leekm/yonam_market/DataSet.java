package net.ddns.leekm.yonam_market;

public class DataSet {
    private String data;
    private String notice;
    private int errorCode;

    public String getData() {
        return data;
    }

    public String getNotice() {
        return notice;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
