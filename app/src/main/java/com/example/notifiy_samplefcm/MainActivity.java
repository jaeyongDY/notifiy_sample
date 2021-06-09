package com.example.notifiy_samplefcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private Button checkToken;
    private TextView setTextToken;
    String newToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkToken = findViewById(R.id.checkToken);
        setTextToken = findViewById(R.id.setTextToken);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.d(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    newToken = task.getResult();
                });

        checkToken.setOnClickListener(v ->{
            Log.d(TAG,"등록되어 있는 토큰 ID : "+ newToken);
            setTextToken.setText(newToken);
            new JSONTask().execute("http://192.168.219.107:3300/post");
        });
    }

    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("token",newToken);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);
                    Log.d("conn","conn1");
                    con = (HttpURLConnection)url.openConnection();
                    Log.d("conn","conn2");
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control","no-cache");
                    con.setRequestProperty("Content-Type","application/json");
                    con.setRequestProperty("Accept","text/html");
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    Log.d("conn","conn3");
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outputStream = con.getOutputStream();
                    // 버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine())!= null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(con!= null){
                        con.disconnect();
                    }
                    try{
                        if(reader != null){
                            reader.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}