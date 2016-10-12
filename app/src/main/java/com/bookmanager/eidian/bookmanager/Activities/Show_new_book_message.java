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
import org.jsoup.select.Elements;

/**
 * Created by clmiberf on 2016/9/26.
 */
public class Show_new_book_message extends Activity {

    static  final int SHOW_RESPONSE = 0;

    private TextView view;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    view.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_new_book_msg);
        view = (TextView) findViewById(R.id.show_new_book_msg);
        Intent intent = getIntent();
        final String path = intent.getStringExtra("pathUrl");
        Log.d("Show_new_book_message",path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection in = new InternetConnection(path,path);
                Log.d("Show_new_book_message",in.getResponse());

                org.jsoup.nodes.Document document = Jsoup.parse(in.getResponse());

                Elements elements = document.select("td.td1");

                //获取书籍数据,放入StringBuilder中
                StringBuilder builder = new StringBuilder();

                for(int i = 1;i<elements.size();i++){
                    Log.d("5555555555",","+elements.get(i).text()+",");
                    if (elements.get(i).text().equals("题名  ")){
                        builder.append("题名: "+elements.get(i+1).text()).append("\n");
                    }else if (elements.get(i).text().equals("出版发行  ")){
                        builder.append("出版发行: "+elements.get(i+1).text()).append("\n");
                    }else if (elements.get(i).text().equals("载体形态  ")){
                        builder.append("载体形式: "+elements.get(i+1).text()).append("\n");
                    }else if (elements.get(i).text().equals("摘要  ")){
                        builder.append("摘要: "+elements.get(i+1).text());
                    }
                }

//                builder.append("题名: "+elements.get(7).text()).append("\n")
//                        .append("出版发行: "+elements.get(9).text()).append("\n")
//                        .append("载体形式: "+elements.get(11).text()).append("\n")
//                        .append("摘要: "+elements.get(13).text());

                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = builder.toString();
                handler.sendMessage(message);

            }
        }).start();
    }




}
