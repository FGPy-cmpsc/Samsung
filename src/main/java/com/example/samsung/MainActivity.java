package com.example.samsung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.gson.Gson;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    Button bStart, btJustDoIt;
    ParseGson p;
    ProgressBar progressBar;
    TextView text;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler() {   // создание хэндлера
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                text.setText(p.toString());
                text.invalidate();
            }
        };

        bStart = (Button) findViewById(R.id.btStart);
        btJustDoIt = (Button) findViewById(R.id.btJustDoIt);
        text = (TextView) findViewById(R.id.text);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnotherThread anotherThread=new AnotherThread();
                anotherThread.start();

            }
        });
        btJustDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Делаем операцию не в потоке (10с)", Toast.LENGTH_SHORT).show();
                StringBuilder response = new StringBuilder();
                System.out.println("1");
                URL url;
                try {
                    url = new URL("http://jsonplaceholder.typicode.com/posts/1");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    InputStream is = http.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine())!=null){
                        response.append(line);
                        response.append("\n");
                    }
                    http.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text.setText(response.toString());
            }
        });
    }
    private class AnotherThread extends Thread {

        public void run() {
            StringBuilder response = new StringBuilder();
            URL url;
            try {
                url = new URL("http://jsonplaceholder.typicode.com/posts/1");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                InputStream is = http.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }
                http.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = response.toString();
            Gson g = new Gson();
            p = g.fromJson(s, ParseGson.class);
            handler.sendEmptyMessage(1);
        }
    }
    public class ParseGson{
        private int userId;
        private int id;
        private String title;
        private String body;
        @Override
        public String toString() {
            return "ParseGson{" +
                    "userId=" + userId +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", body='" + body + '\'' +
                    '}';
        }

    }
}