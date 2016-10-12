package com.bookmanager.eidian.bookmanager.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by clmiberf on 2016/9/22.
 */
public class ShowBookChineseInfo extends BaseActivity {

    static final int SHOW_CHINESE_INFO = 0;

    private TextView showChineseInfo;

    private ListView listView;


    private Handler handler = new Handler(){

        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_CHINESE_INFO:
                    String response = (String) message.obj;
                    Log.d("ShowBookChineseInfo",response);
                    showChineseInfo.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_chinese_information_layout);
        showChineseInfo = (TextView) findViewById(R.id.show_chinese_info);
        Intent intent = getIntent();
        final String titleUrl = intent.getStringExtra("titleUrl");

        final String path = intent.getStringExtra("path");
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection in = new InternetConnection(titleUrl,path);
                String responseInformation = in.getResponse();
                Log.d("ShowBookCineseInfo1",responseInformation);
                //解析返回的数据
                Document document = Jsoup.parse(responseInformation);
                Elements elements = document.getElementsByClass("td1");
                //存放书籍详细信息
                Log.d("ShowBookChineseInfo2",elements.get(0).text());
                StringBuilder sb = new StringBuilder();


                sb.append("题名: "+elements.get(7).text()+"\n");
                sb.append("出版社: "+elements.get(9).text()+"\n");
                sb.append("载体: "+elements.get(11 ).text()+"\n");
                sb.append("摘要: "+elements.get(15).text()+"\n");
                sb.append("主题: "+elements.get(21).text()+"\n");


                Message message = new Message();
                message.what = SHOW_CHINESE_INFO;
                message.obj = sb.toString();
                handler.sendMessage(message);
//                        Intent intent = new Intent(SearchBook.this,ShowBookChineseInfo.class);
//                        intent.putExtra("information",sb.toString());
//
//                        Log.d("SearchBook",responseInformation);
//                        startActivity(intent);
                    }
                }).start();
            }
}
