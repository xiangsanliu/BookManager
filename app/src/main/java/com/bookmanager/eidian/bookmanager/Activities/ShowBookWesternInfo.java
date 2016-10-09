package com.bookmanager.eidian.bookmanager.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by clmiberf on 2016/9/25.
 */
public class ShowBookWesternInfo extends Activity {

    static final int SHOW=0;

    private TextView westInfo;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW:
                    String response = (String) message.obj;
                    westInfo.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_western_info_layout);
        westInfo = (TextView) findViewById(R.id.show_western_info);
        Intent intent = getIntent();
        final String titleWesternUrl = intent.getStringExtra("titleWesternUrl");
        final String path = intent.getStringExtra("path");
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection in = new InternetConnection(titleWesternUrl,path);
                Document document = Jsoup.parse(in.getResponse());
                Elements elements = document.getElementsByClass("td1");
                StringBuilder sb = new StringBuilder();
                Log.d("ShowWesternInfo1", String.valueOf(elements.size()));
                Log.d("ShowWesternInfo2",elements.text());
                sb.append("题名: "+elements.get(1));

                Message message = new Message();
                message.what = SHOW;
                message.obj = elements.toString();
                handler.sendMessage(message);
            }
        }).start();

    }
}
