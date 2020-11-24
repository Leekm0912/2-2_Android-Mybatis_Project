package net.ddns.leekm.yonam_market;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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
        if(!result.equals("success")) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
        //tv_outPut.setText(s);
    }

    public int execute_return(String s){

        return 0;
    }
}
