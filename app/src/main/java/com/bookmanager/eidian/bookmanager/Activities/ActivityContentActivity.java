package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.Connection;
import com.bookmanager.eidian.bookmanager.R;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ActivityContentActivity extends BaseActivity {

    private static final int SHOW_RESPONSE = 0;
    String u;
    Bitmap bitmap;
    ImageView imageView;
    String newsTitle;
    TextView newsTitleView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOW_RESPONSE:
                    Object newsResponse = msg.obj;
                    TextView textView = (TextView) findViewById(R.id.content_text);
                    textView.setText(newsResponse.toString());
                    break;
                case 1:
                    String photoUrl = (String) msg.obj;
                    Picasso.with(imageView.getContext()).load(photoUrl).into(imageView);
                    break;
                case 2:
                    String date_str = (String) msg.obj;
                    ((TextView) findViewById(R.id.news_date)).setText(date_str);
            }
        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.image);
        final Intent intent = getIntent();
        final String contentUrl = intent.getStringExtra("url");
        final String fragmentCode = intent.getStringExtra("fragmentCode");
        newsTitle = intent.getStringExtra("newsTitle");
        newsTitleView = (TextView) findViewById(R.id.news_content_title);
        newsTitleView.setText(newsTitle);

        if (fragmentCode.equals("NoticeFragment")) {
            imageView.setVisibility(View.GONE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                HttpURLConnection connection1;
                String photoUrl = intent.getStringExtra("photoUrl");
                try {
                    connection = Connection.getConnectionToHZAUlib(contentUrl);

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    String datas = response.toString();

                    Document document = Jsoup.parse(datas);
                    Elements elements = document.select("div.table-responsive");
                    Elements date = document.select("h5.hn_news_subtitle");
                    String str = date.text();
                    str = str.substring(0,17);

                    Elements element = elements.get(0).getElementsByTag("p");
                    String content = "";
                    for (int i = 0; i < element.size(); i++) {
                        String title = element.get(i).getElementsByTag("p").text();
                        content += "        " + title + '\n';
                    }
                    if (fragmentCode.equals("ActivityForecastFragment")) {
                        photoUrl = "http://lib.hzau.edu.cn" + elements.get(0).getElementsByTag("img").attr("src");
                    }
                    connection.disconnect();
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    //将服务器返回的数据存放到Message中
                    message.obj = content;
                    handler.sendMessage(message);


                    Message message1 = new Message();
                    message1.what = 1;
                    message1.obj = photoUrl;
                    handler.sendMessage(message1);

                    Message message2 = new Message();
                    message2.what = 2;
                    message2.obj = str;
                    handler.sendMessage(message2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
