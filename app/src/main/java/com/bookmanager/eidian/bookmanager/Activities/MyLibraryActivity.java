package com.bookmanager.eidian.bookmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryActivity extends BaseActivity {

    private TextView money;
    private Button borrowing_num;
    private Button history_num;
    private Button booked_num;
    private Button pre_num;

    private List<String> contentList ;

    private String[] content = new String[10];

    static  final  int SHOW_RESPONSE = 0;


    //当未获取到myLibrary信息时
    int al=0;
    private AlertDialog.Builder dialog;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_RESPONSE:
                    String show[] ;
                    show = (String[]) message.obj;
                    if (show != null){
                        money.setText(show[0]);
                        borrowing_num.setText(show[1]);
                        history_num.setText(show[2]);
                        booked_num.setText(show[3]);
                        pre_num.setText(show[4]);
                        borrowing_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyLibraryActivity.this, BorrowingContent.class);
                                intent.putExtra("url", content[5]);
                                startActivity(intent);
                            }
                        });
                        history_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyLibraryActivity.this, MyLibraryContent.class);
                                intent.putExtra("url", content[6]);
                                startActivity(intent);
                            }
                        });
                        booked_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyLibraryActivity.this, MyLibraryContent.class);
                                intent.putExtra("url", content[7]);
                                startActivity(intent);
                            }
                        });
                        pre_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyLibraryActivity.this, MyLibraryContent.class);
                                intent.putExtra("url", content[8]);
                                startActivity(intent);
                            }
                        });


                    }else{
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                        dialog.show();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        money = (TextView) findViewById(R.id.money);
        borrowing_num = (Button) findViewById(R.id.borrowing_num);
        history_num = (Button) findViewById(R.id.history_num);
        booked_num = (Button) findViewById(R.id.booked_num);
        pre_num = (Button) findViewById(R.id.pre_num);

        Intent intent = getIntent();
        final String myLibrary1 = intent.getStringExtra("myLibrary");
        final String str = intent.getStringExtra("str");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用AlertDialog当获取信息出错时弹出信息
                dialog = new AlertDialog.Builder(MyLibraryActivity.this);
                dialog.setTitle("哎呀!出错啦~");
                //存放myLibrary中的信息
                contentList = new ArrayList<String>();
                InternetConnection in = new InternetConnection(myLibrary1, myLibrary1);
                String response = in.getResponse();
                if (response != null) {
                    Document document = Jsoup.parse(response);
                    String url;
                    content[0] = "当前过期外借欠款："+ document.select("td.td1").last().text();
                    content[1] = "当前在借书籍数：" + document.select("td.td1").get(0).text();
                    content[2] = "历史借阅书籍数：" + document.select("td.td1").get(1).text();
                    content[3] = "预约请求：" + document.select("td.td1").get(2).text();
                    content[4] = "预定请求：" + document.select("td.td1").get(3).text();
                    for (int i = 1;i<=4;i++) {
                        url = document.select("td.td1").get(i-1).getElementsByTag("td").get(0).getElementsByTag("a").attr("href");
                        content[4+i] = url.substring(24, url.length()-3);
                        Log.d("url", 4+i+" "+url.substring(24, url.length()-3));
                    }

                }else{
                    dialog.setMessage("我的图书馆内容获取失败"+"\n");
                    al++;
                }

                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = content;
                handler.sendMessage(message);

            }
        }).start();


    }
}
