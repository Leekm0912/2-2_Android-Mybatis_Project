package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SellPage extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    TextView t;
    EditText title;
    EditText price;
    TextInputEditText text;
    RadioGroup radioGroup;
    //img관련
    Button imageUpload;
    ImageView imgVwSelected;
    File tempSelectFile;
    byte[] fileArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_page);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        title = findViewById(R.id.title_input);
        price = findViewById(R.id.price_input);
        text = findViewById(R.id.content);
        radioGroup = findViewById(R.id.choose_group);
        imageUpload = findViewById(R.id.imageUpload);
        imgVwSelected = findViewById(R.id.imageView);
    }

    public void postSubmit(View v) {
        Map<String, String> param = new HashMap<>();
        RadioButton rb = findViewById(radioGroup.getCheckedRadioButtonId());
        String url = "http://220.66.111.200:8889/yonam-market/market/postUpload.jsp";
        String parse_data = null;
        ContentValues contentValues = new ContentValues();
        // AsyncTask를 통해 HttpURLConnection 수행.
        try {
            AppData appData = (AppData) getApplication();

            param.put("가격",price.getText().toString());
            param.put("제목", title.getText().toString());
            param.put("내용", text.getText().toString());
            param.put("게시판", rb.getText().toString());
            param.put("ID", appData.getUser().getID());

            int price_value = Integer.parseInt(price.getText().toString());
            contentValues.put("제목", title.getText().toString());
            contentValues.put("가격", price_value);
            contentValues.put("내용", text.getText().toString());
            contentValues.put("게시판", rb.getText().toString());
            contentValues.put("ID", appData.getUser().getID());
        }
        /*catch (NumberFormatException ne){ //숫자가 아닌값을 price에 입력했을때.
            ne.printStackTrace();
            Toast.makeText(this, "가격은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
        }*/
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "빈칸이 있거나 잘못된 가격입니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            //File이 널이 아니면 이미지 전송
            if(tempSelectFile != null) {
                String result = DoFileUpload("http://220.66.111.200:8889/yonam-market/market/img_upload/uploadAction.jsp",tempSelectFile, param);  //해당 함수를 통해 이미지 전송.
                Parse p = new Parse((AppData)getApplication() ,result);
                if(p.getNotice().equals("success")){
                    //Intent intent = new Intent(this,MainMenu.class);
                    //startActivityForResult(intent,0);//액티비티 띄우기
                    Log.i("삽입완료","삽입완료");
                    Toast.makeText(this,"그림+게시물 작성 완료",Toast.LENGTH_SHORT).show();
                }else{
                    return;
                }
            }else{
                NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
                try {
                    parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                    Log.i("1",parse_data);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Parse p = new Parse((AppData)getApplication() ,parse_data);
                if(p.getNotice().equals("success")){
                    //Intent intent = new Intent(this,MainMenu.class);
                    //startActivityForResult(intent,0);//액티비티 띄우기
                    Log.i("삽입완료","삽입완료");
                    Toast.makeText(this,"게시물 작성 완료",Toast.LENGTH_SHORT).show();
                }
                else{
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        finish();
    }

    public void imgUpload(View v){
        Intent intent = new Intent();
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        Uri dataUri = data.getData();
        imgVwSelected.setImageURI(dataUri);
        try {
            // ImageView 에 이미지 출력
            InputStream in = getContentResolver().openInputStream(dataUri);
            Bitmap image = BitmapFactory.decodeStream(in);
            imgVwSelected.setImageBitmap(image);
            in.close();
            // 선택한 이미지 임시 저장
            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            tempSelectFile = new File(Environment.getExternalStorageDirectory() + "/", "temp_" + date + ".jpeg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //btnImageSend.setEnabled(true);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }

// 여기서부터가 jsp 이미지를 보내는 코드입니다.

    public String DoFileUpload(String urlString, File file, Map<String, String> params) {
        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        String stringBuffer = ""; // 결과(html문서)를 담을 변수

        try {
            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String, String> param : params.entrySet()) {
                if(postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            Log.i("==========================파라미터========================",postData.toString());
            urlString = urlString + "?" + postData.toString();

            File sourceFile = file;

            DataOutputStream dos;

            if (!sourceFile.isFile()) {

                Log.e("uploadFile", "Source File not exist :" + sourceFile.getName());

            } else {

                FileInputStream mFileInputStream = new FileInputStream(sourceFile);

                URL connectUrl = new URL(urlString);

                // open connection

                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

                conn.setDoInput(true);

                conn.setDoOutput(true);

                conn.setUseCaches(false);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");

                conn.setRequestProperty("ENCTYPE", "multipart/form-data");

                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", sourceFile.getName());

                conn.setRequestProperty("price",params.get("가격"));
                conn.setRequestProperty("title",params.get("제목"));
                conn.setRequestProperty("text",params.get("내용"));
                conn.setRequestProperty("board",params.get("게시판"));
                conn.setRequestProperty("ID",params.get("ID"));

                // write data

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFile.getName() + "\"" + lineEnd);

                dos.writeBytes(lineEnd);



                int bytesAvailable = mFileInputStream.available();

                int maxBufferSize = 1024 * 1024;

                int bufferSize = Math.min(bytesAvailable, maxBufferSize);



                byte[] buffer = new byte[bufferSize];

                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);



                // read image

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);

                    bytesAvailable = mFileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                }



                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                mFileInputStream.close();

                dos.flush(); // finish upload...



                if (conn.getResponseCode() == 200) {

                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");

                    BufferedReader reader = new BufferedReader(tmp);

                    String line;

                    while ((line = reader.readLine()) != null) {

                        stringBuffer += line;

                    }

                }

                mFileInputStream.close();

                dos.close();


            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        Log.i("========결과 Line=====",stringBuffer);
        return stringBuffer;
    }

}
