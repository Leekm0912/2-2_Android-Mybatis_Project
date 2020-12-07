package net.ddns.leekm.yonam_market;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

// 서버에 작업을 요청해주는 클래스. 결과로 String타입 작업결과(HTML문서)를 리턴
public class NetworkTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private String url;
    private ContentValues values;
    private AppData appData;

    public NetworkTask(Context context ,String url, ContentValues values, AppData appData) {
        this.context = context;
        this.url = url;
        this.values = values;
        this.appData = appData;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        Parse p = new Parse(appData, s);
        String result = p.getNotice();
        if(!result.equals("success")) { // 결과가 success가 아니라면 서버에서 전달하는 오류를 사용자에게 보여줌
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }
}
