package net.ddns.leekm.yonam_market;

import android.app.Application;

public class AppData extends Application {
    public static final String SERVER_URL = "http://220.66.115.40";
    public static final String SERVER_PORT = "5005";
    public static final String SERVER_FULL_URL = SERVER_URL+":"+SERVER_PORT;
    private 사용자 user = new 사용자();
    public 사용자 getUser() {
        return user;
    }

    public void setUser(사용자 user) {
        this.user = user;
    }


}
