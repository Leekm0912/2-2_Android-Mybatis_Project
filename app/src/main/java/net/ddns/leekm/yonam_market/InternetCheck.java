package net.ddns.leekm.yonam_market;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class InternetCheck extends AsyncTask<Void,Void,Boolean> {
    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) { try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress(AppData.SERVER_IP, Integer.parseInt(AppData.SERVER_PORT)), 1500);
        sock.close();
        Log.i("====================서버연결 성공======================","");
        return true;
    } catch (IOException e) { e.printStackTrace(); return false; } }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}